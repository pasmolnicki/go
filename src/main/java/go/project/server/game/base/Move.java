package go.project.server.game.base;


// Represents a move in Go, basically a coordinate
// with a color attached to it
public class Move {
    private short x;
    private short y;
    private Color color;
    
    // Parses a move from a string representation:
    // xxXX where xx is the row (0-based), XX is the column (0-based)
    public Move(final String fmt) 
        throws IllegalArgumentException, NumberFormatException {
        if (fmt.length() != 4) {
            throw new IllegalArgumentException("Invalid move format");
        }

        this.x = Short.parseShort(fmt.substring(0, 2));
        this.y = Short.parseShort(fmt.substring(2, 4));
        this.color = Color.NONE; // Color to be set later
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

    @Override
    final public String toString() {
        return String.format("%02d%02d", x, y);
    }
}
