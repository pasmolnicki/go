package go.project.server.game;

import java.util.Vector;

import go.project.common.BoardLike;
import go.project.common.Move;

// Has extended BoardLike features, like move validation, generating legal moves, etc.
public interface ExtBoardLike extends BoardLike {
    // generates all legal moves for the current side
    Vector<Move> generateLegalMoves();
}
