package go.project.server.server;

import go.project.server.game.base.Color;

public class MatchClientData extends ClientData {
    private String matchId;
    private Color side;

    public MatchClientData(java.net.Socket conn) {
        super(conn);
    }

    public MatchClientData(final ClientData clientData, Color side, String matchId) {
        super(clientData);
        this.matchId = matchId;
        this.side = side;
    }

    final public String getMatchId() {
        return matchId;
    }

    final public Color getSide() {
        return side;
    }
}
