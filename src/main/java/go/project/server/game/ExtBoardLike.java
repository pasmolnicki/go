package go.project.server.game;

import java.util.Vector;

import go.project.server.game.base.BoardLike;
import go.project.server.game.base.Move;

// Has extended BoardLike features, like move validation, generating legal moves, etc.
public interface ExtBoardLike extends BoardLike {
    // generates all legal moves for the current side
    Vector<Move> generateLegalMoves();
}
