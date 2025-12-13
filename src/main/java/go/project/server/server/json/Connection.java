package go.project.server.server.json;

// JSON representation of a connection
// Contains the clientId
// Used to send the clientId to the client upon connection
public final class Connection {
    public String clientId;

    public Connection(String clientId) {
        this.clientId = clientId;
    }
}
