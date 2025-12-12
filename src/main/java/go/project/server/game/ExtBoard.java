package go.project.server.game;

import java.util.Vector;

import go.project.server.game.base.Board;
import go.project.server.game.base.Color;
import go.project.server.game.base.Move;

// Actual Board representation for the backend
final public class ExtBoard extends Board implements ExtBoardLike {
    public ExtBoard(final int size) {
        super(size);
    }

    @Override
    public Vector<Move> generateLegalMoves() {
        Vector<Move> legalMoves = new Vector<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == Color.NONE) {
                    legalMoves.add(new Move((short)i, (short)j, currentSide));
                }
            }
        }
        return legalMoves;
    }    
}
