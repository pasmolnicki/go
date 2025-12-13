package go.project.server.game.base;

// Has Board representation of the Go game,
// stored as a 2D array of intersections
public class Board implements BoardLike {
    protected Color[][] grid;
    protected Color currentSide;
    protected int size;
    protected Termination terminationStatus = Termination.ONGOING;

    public Board(int size) {
        this.resize(size);
    }

    @Override
    public Color getCurrentSide() {
        return currentSide;
    }

    @Override
    public int resize(int newSize) {
        this.size = newSize;
        this.grid = new Color[newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                grid[i][j] = Color.NONE;
            }
        }
        this.currentSide = Color.BLACK; // Reset to Black
        return newSize;
    }

    @Override
    public final Color[][] getGrid() {
        return grid;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void playMove(final Move move) {
        // TODO: implement 1-3 points
        if (move.isPass()) {
            return;
        }
    }

    @Override
    public boolean isLegalMove(final Move move) {
        return move.isPass() || grid[move.getX()][move.getY()] == Color.NONE;
    }

    @Override
    public boolean isTerminal() {
        // TODO: implement
        return false;
    }

    @Override
    public Termination getTermination() {
        return terminationStatus;
    }
}
