package go.project.server.server;

import java.io.PrintWriter;
import java.util.Scanner;

import go.project.server.game.ExtBoard;
import go.project.server.game.base.Config;
import go.project.server.game.base.Move;
import go.project.server.server.json.JsonFmt;
import go.project.server.server.json.PlayerTurn;
import go.project.server.game.base.Color;
import go.project.server.server.json.*;

// Represents a match between two players
public class Match implements Runnable {
    private MatchClientData black;
    private MatchClientData white;
    private ExtBoard board;
    private final String matchId;
    private State state;

    public static enum State {
        ONGOING,
        COMPLETED,
        ABORTED
    }

    public Match(ClientData cl1, ClientData cl2) {
        this.state = State.ONGOING;
        this.matchId = java.util.UUID.randomUUID().toString();
        this.board = new ExtBoard(Config.DEFAULT_BOARD_SIZE); // Standard 19x19 board

        this.black = new MatchClientData(cl1, Color.BLACK, matchId);
        this.white = new MatchClientData(cl2, Color.WHITE, matchId);
    }

    @Override
    public void run() {
        State endState = State.COMPLETED;
        // Run the match until completion
        try {
            PrintWriter blackOut = new PrintWriter(black.getConnection().getOutputStream(), true);
            Scanner blackIn = new Scanner(black.getConnection().getInputStream());

            PrintWriter whiteOut = new PrintWriter(white.getConnection().getOutputStream(), true);
            Scanner whiteIn = new Scanner(white.getConnection().getInputStream());

            // Notify players of match start
            blackOut.println(JsonFmt.toJson(new PlayerTurn(Color.BLACK)));
            whiteOut.println(JsonFmt.toJson(new PlayerTurn(Color.WHITE)));

            // Main game loop (simplified)
            while (getState() == State.ONGOING) {
                blackOut.println(JsonFmt.toJson(handleInput(black, blackIn)));
                whiteOut.println(JsonFmt.toJson(handleInput(white, whiteIn)));
            }   
        } catch (Exception e) {
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

    private GameResponse handleInput(MatchClientData client, Scanner in) {
        String response = GameResponse.MESSAGE_MOVE_OK;
        int status = GameResponse.STATUS_OK;

        try {
            // Should be either move or resign command
            String line = in.nextLine();
            GameCommand<?> 
                command = JsonFmt.fromJson(line, GameCommand.class);

            if (command.payload instanceof GameCommand.PayloadMakeMove) {
                try {
                    GameCommand.PayloadMakeMove payload = (GameCommand.PayloadMakeMove) command.payload;
                    Move move = Move.parseMove(payload.move, board.getSize(), board.getCurrentSide());
                } catch (NumberFormatException e) {
                    System.out.println("Number format error in move from player " + command.playerid + ": " + e.getMessage());
                    status = GameResponse.STATUS_ERROR;
                    response = GameResponse.MESSAGE_INVALID_MOVE;
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal move from player " + command.playerid + ": " + e.getMessage());
                    status = GameResponse.STATUS_ERROR;
                    response = GameResponse.MESSAGE_INVALID_MOVE;
                }
            } else {
                System.out.println("Unknown command from player " + command.playerid);
            }

        } catch (Exception e) {
            e.printStackTrace();
            close(client, GameResponse.STATUS_ERROR, GameResponse.MESSAGE_INTERNAL_ERROR);
            this.state = State.ABORTED;
            return new GameResponse(GameResponse.STATUS_ERROR, GameResponse.MESSAGE_INTERNAL_ERROR);
        }

        return new GameResponse(status, response);
    }

    private void close(MatchClientData client, int status, String message) {
        try {
            if (client.getConnection().isClosed()) return;

            PrintWriter out = new PrintWriter(client.getConnection().getOutputStream(), true);
            out.println(JsonFmt.toJson(new GameResponse(status, message)));
            client.getConnection().close();

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
