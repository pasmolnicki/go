package go.project.server.server;

import java.net.Socket;

import go.project.server.server.json.Connection;

/**
 * Represents data associated with a connected client.
 * Holds the client's connection and JSON-serializable data.
 */
public class ClientData {

    // JSON representation of client data
    public static class JsonClientData extends Connection{
        private String nickname;

        public JsonClientData(final JsonClientData other) {
            super(other.clientId);
            this.nickname = other.nickname;
        }

        public JsonClientData() {
            super(java.util.UUID.randomUUID().toString());
            this.nickname = "";
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    private JsonClientData clientData = new JsonClientData();
    private Socket conn;

    public ClientData(final ClientData other) {
        this.conn = other.conn;
        this.clientData = new JsonClientData(other.clientData);
    }

    /**
     * Initializes ClientData with the given socket connection.
     */
    public ClientData(Socket conn) {
        this.conn = conn;
    }

    /**
     * Gets the JSON-serializable client data.
     */
    public JsonClientData data() {
        return clientData;
    }

    final public Socket getConnection() {
        return conn;
    }
}
