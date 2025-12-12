package go.project.server.game.base;

// Represents behavior common to all Board-like entities
public interface BoardLike {
    Color getCurrentSide(); // returns which side is to play now
    int resize(int newSize); // resizes the board to newSize x newSize
    int getSize(); // returns current board size (e.g., 19 for a 19x19 board)
    void playMove(final Move move); // plays a move on the board, doesn't check the legality
    boolean isLegalMove(final Move move); // checks if a move is legal
    boolean isTerminal(); // checks if the game is over

    // returns the termination status of the game, has
    // termination reason only if isTerminal() is true
    Termination getTermination();
}
