package go.project.server.server.json;

import go.project.server.game.base.Color;


/**
 * JSON representation of a player's turn
 * Returned on game start to indicate which side the player is on
 */
public class PlayerTurn {
    private String side;

    /**
     * Initializes PlayerTurn with the given Color enum.
     */
    public PlayerTurn(Color side) {
        switch (side) {
            case BLACK:
                this.side = "BLACK";
                break;
            case WHITE:
                this.side = "WHITE";
                break;
            default:
                break;
        }
    }

    public PlayerTurn() {}

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    /**
     * Converts the side string to a Color enum.
     * @return Corresponding Color enum value.
     */
    public Color asColor() {
        switch(this.side) {
            case "BLACK":
                return Color.BLACK;
            case "WHITE":
                return Color.WHITE;
            default:
                return Color.NONE;
        }
    }
}
