package go.project.server.game.base;


// Represents a move in Go, basically a coordinate
// with a color attached to it
public class Move {
    private short x;
    private short y;
    private Color color;

    // Same as the constructor above, but static
    static final public Move parseMove(final String fmt, int size, Color currentSide) 
        throws IllegalArgumentException, NumberFormatException {
        return new Move(fmt, size, currentSide);
    }
    
    // Parses a move from a string representation:
    // xxXX where xx is the row (0-based), XX is the column (0-based)
    public Move(final String fmt, int size, Color currentSide) 
        throws IllegalArgumentException, NumberFormatException {
        if (fmt.length() != 4) {
            throw new IllegalArgumentException("Invalid move format");
        }

        this.x = Short.parseShort(fmt.substring(0, 2));
        this.y = Short.parseShort(fmt.substring(2, 4));

        if (this.x < 0 || this.x > size || this.y < 0 || this.y > size) {
            throw new IllegalArgumentException("Move out of bounds");
        }

        this.x--;
        this.y--;

        if (this.x != this.y && (this.x == -1 || this.y == -1)) {
            throw new IllegalArgumentException("Invalid pass move");
        }

        this.color = currentSide; // Color to be set later
    }

    public Move(short x, short y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    final public short getX() {
        return x;
    }

    final public short getY() {
        return y;
    }

    final public Color getColor() {
        return color;
    }

    final public boolean isPass() {
        return x == -1 && y == -1;
    }

    @Override
    final public String toString() {
        return String.format("%02d%02d", x, y);
    }
}
