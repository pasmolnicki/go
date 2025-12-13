package go.project.server.server.json;

import go.project.server.game.base.Color;

public class PlayerTurn {
    public String side;
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
}
