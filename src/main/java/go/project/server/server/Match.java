package go.project.server.server;

import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import go.project.server.game.ExtBoard;
import go.project.common.Config;
import go.project.common.Move;
import go.project.common.json.*;
import go.project.common.Color;

// Represents a match between two players
public class Match implements Runnable {

    private static class GameThread extends Thread {
        private Match parent;
        private MatchClientData data;
        private PrintWriter out;
        private Scanner in;

        public GameThread(Match parent, MatchClientData data, PrintWriter out, Scanner in) {
            this.parent = parent;
            this.data = data;
            this.out = out;
            this.in = in;
        }

        private void log(String msg) {
            Logger.getInstance().log("Match-" + parent.matchId, msg);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    out.println(JsonFmt.toJson(handleInput(data, in)));
                }
            } catch (Exception e) {
                // Only log if not caused by input closure
                if (!(e instanceof NoSuchElementException)) {
                    e.printStackTrace();
                }
                parent.childException = e;
                parent.matchThread.interrupt();
            }
        }

        /**
         * Handles input from a player client.
         * @param client The client data.
         * @param in The input scanner for the client.
         * @return GameResponse indicating the result of the command.
         * @throws Exception Internal error processing the command.
         * @throws NoSuchElementException If the input is closed.
         */
        private GameResponse handleInput(MatchClientData client, Scanner in) throws Exception, NoSuchElementException {
            // Should be either move or resign command
            String line = in.nextLine();
            GameCommand<?> 
                command = JsonFmt.fromJson(line, GameCommand.class);
            Object payload = command.getPayload();

            if (payload instanceof GameCommand.PayloadMakeMove) {
                return processMove(client, (GameCommand.PayloadMakeMove)payload);
            }

            log("Unknown command from player " + client.data().getClientId() + ": " + command.getCommand());
            return new GameResponse(GameResponse.STATUS_ERROR, GameResponse.MESSAGE_UNKNOWN_COMMAND);
        }

        /**
         * Processes a move command from a player.
         * @param client
         * @param payload
         * @return GameResponse indicating success or failure of the move.
         */
        private GameResponse processMove(MatchClientData client, GameCommand.PayloadMakeMove payload) {
            try {
                synchronized (parent.board) {
                    if (!parent.board.getCurrentSide().equals(client.getSide())) {
                        log("It's not player " + client.data().getClientId() + " turn.");
                        return new GameResponse(GameResponse.STATUS_ERROR, GameResponse.MESSAGE_NOT_YOUR_TURN);
                    }

                    Move move = Move.parseMove(payload.getMove(), parent.board.getSize(), parent.board.getCurrentSide());
                    parent.board.playMove(move);
                }
                log("Player " + client.data().getClientId() + " played move: " + payload.getMove());
                return new GameResponse(GameResponse.STATUS_OK, GameResponse.MESSAGE_MOVE_OK);
            }  catch (IllegalArgumentException e) {
                log("Illegal move from player " + client.data().getClientId() + ": " + e.getMessage());
                return new GameResponse(GameResponse.STATUS_ERROR, GameResponse.MESSAGE_INVALID_MOVE);
            }
        }
    }

    private Thread matchThread;
    private MatchClientData black;
    private MatchClientData white;
    private ExtBoard board;
    volatile private Exception childException;
    private final String matchId;
    private State state;
    private GameThread blackThread;
    private GameThread whiteThread;

    // Possible states of a match,
    // Note that once a match is COMPLETED or ABORTED
    // it should be cleaned up by MatchManager
    public static enum State {
        ONGOING,
        COMPLETED, // Match finished normally
        ABORTED // Error or player disconnected
    }

    public Match(ClientData cl1, ClientData cl2) {
        this.state = State.ONGOING;
        this.matchId = java.util.UUID.randomUUID().toString();
        this.board = new ExtBoard(Config.DEFAULT_BOARD_SIZE); // Standard 19x19 board

        this.black = new MatchClientData(cl1, Color.BLACK, matchId);
        this.white = new MatchClientData(cl2, Color.WHITE, matchId);
    }

    private static void log(String msg) {
        Logger.getInstance().log("Match", msg);
    }

    @Override
    public void run() {
        State endState = State.COMPLETED;
        matchThread = Thread.currentThread();

        // Run the match until completion
        try {
            PrintWriter blackOut = new PrintWriter(black.getConnection().getOutputStream(), true);
            Scanner blackIn = new Scanner(black.getConnection().getInputStream());

            PrintWriter whiteOut = new PrintWriter(white.getConnection().getOutputStream(), true);
            Scanner whiteIn = new Scanner(white.getConnection().getInputStream());

            // Notify players of match start
            blackOut.println(JsonFmt.toJson(new PlayerTurn(Color.BLACK)));
            whiteOut.println(JsonFmt.toJson(new PlayerTurn(Color.WHITE)));
            
            // Support async input reading to disallow blocking
            // (Always respond to both players even if it's not their turn)
            blackThread = new GameThread(this, black, blackOut, blackIn);
            whiteThread = new GameThread(this, white, whiteOut, whiteIn);           

            blackThread.start();
            whiteThread.start();

            try {
                blackThread.join();
                whiteThread.join();
            } catch (InterruptedException ie) {
                // Interrupted - likely due to error in child thread
                if (childException != null) {
                    throw childException;
                }
            }

        } catch(NoSuchElementException e) {
            // Thrown by Scanner when input is closed
            log("A player disconnected: " + e.getMessage());
            endState = State.ABORTED;
        } catch (Exception e) {
            // Other exceptions - internal errors
            e.printStackTrace();
            endState = State.ABORTED;
        } finally {
            if (endState == State.ABORTED) {
                close(black, GameResponse.STATUS_ERROR, GameResponse.MESSAGE_INTERNAL_ERROR);
                close(white, GameResponse.STATUS_ERROR, GameResponse.MESSAGE_INTERNAL_ERROR);
            } else {
                // TODO: Return final scores
                close(black, GameResponse.STATUS_OK, GameResponse.MESSAGE_MATCH_ENDED);
                close(white, GameResponse.STATUS_OK, GameResponse.MESSAGE_MATCH_ENDED);
            }
        }
        this.state = endState;
    }

    /**
     * Closes a client connection with a final message (if not already closed).
     */
    private void close(MatchClientData client, int status, String message) {
        try {
            if (client.getConnection().isClosed()) return;

            PrintWriter out = new PrintWriter(client.getConnection().getOutputStream(), true);
            log("Closing connection to player " + client.data().getClientId() + " with status " + status);
            out.println(JsonFmt.toJson(new GameResponse(status, message)));
            client.getConnection().close();

            if (blackThread != null && blackThread.isAlive()) {
                blackThread.interrupt();
            }
            if (whiteThread != null && whiteThread.isAlive()) {
                whiteThread.interrupt();
            }


            // TODO: Do something with ClientHandler to update its state
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public State getState() {
        return state;
    }

    public String getMatchId() {
        return matchId;
    }
}
