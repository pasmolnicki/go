package go.project.server.server;

import java.net.Socket;

public class ClientData {
    private String nickname;
    private String clientId;
    private Socket conn;

    public ClientData(final ClientData other) {
        this.conn = other.conn;
        this.clientId = other.clientId;
        this.nickname = other.nickname;
    }

    public ClientData(Socket conn) {
        this.conn = conn;
        this.clientId = java.util.UUID.randomUUID().toString();
    }

    final public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    final public String getNickname() {
        return nickname;
    }

    final public String getClientId() {
        return clientId;
    }

    final public Socket getConnection() {
        return conn;
    }
}
