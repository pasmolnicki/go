package go.project.server.server;

import java.net.Socket;

import go.project.common.json.Connection;

/**
 * Represents data associated with a connected client.
 * Holds the client's connection and JSON-serializable data.
 */
public class ClientData {

    private Connection clientData;
    private Socket conn;

    public ClientData(final ClientData other) {
        this.conn = other.conn;
        this.clientData = new Connection(other.clientData.getClientId());
    }

    /**
     * Initializes ClientData with the given socket connection.
     */
    public ClientData(Socket conn) {
        this.conn = conn;
        this.clientData = new Connection(java.util.UUID.randomUUID().toString());
    }

    /**
     * Gets the JSON-serializable client data.
     */
    public Connection data() {
        return clientData;
    }

    final public Socket getConnection() {
        return conn;
    }
}
