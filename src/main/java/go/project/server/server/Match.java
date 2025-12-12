package go.project.server.server;

import java.io.PrintWriter;
import java.util.Scanner;

import go.project.server.game.ExtBoard;
import go.project.server.game.base.Config;
import go.project.server.game.base.Color;

// Represents a match between two players
public class Match implements Runnable {
    private MatchClient black;
    private MatchClient white;
    private ExtBoard board;
    private final String matchId;

    public Match(ClientData cl1, ClientData cl2) {
        this.matchId = java.util.UUID.randomUUID().toString();
        this.board = new ExtBoard(Config.DEFAULT_BOARD_SIZE); // Standard 19x19 board

        this.black = new MatchClient(cl1, Color.BLACK, matchId);
        this.white = new MatchClient(cl2, Color.WHITE, matchId);
    }

    @Override
    public void run() {
        // Run the match until completion
        try {
            PrintWriter blackOut = new PrintWriter(black.getConnection().getOutputStream(), true);
            Scanner blackIn = new Scanner(black.getConnection().getInputStream());

            PrintWriter whiteOut = new PrintWriter(white.getConnection().getOutputStream(), true);
            Scanner whiteIn = new Scanner(white.getConnection().getInputStream());

            // Notify players of match start
            blackOut.println("MATCH_START BLACK " + matchId);
            whiteOut.println("MATCH_START WHITE " + matchId);

            // Main game loop (simplified)
            while (true) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
