package go.project.server.server;

import java.util.HashMap;
import java.util.Vector;


/**
 * Manages all connected clients.
 */
public class ClientManager {
    private HashMap<String, ClientHandler> clients;

    public ClientManager() {
        clients = new HashMap<>();
    }

    /**
     * Add a new client
     */
    public final void addClient(final ClientHandler client) {
        clients.put(client.getClientData().data().getClientId(), client);
    }

    /**
     * Remove a client by clientId
     */
    public final void removeClient(final String clientId) {
        clients.remove(clientId);
    }

    /**
     * Get a list of clients waiting for a match
     * @return Vector of awaiting clients (not null)
     */
    public final Vector<ClientHandler> awaitingClients() {
        Vector<ClientHandler> awaiting = new Vector<>();
        for (ClientHandler client : clients.values()) {
            if (client.isWaitingForMatch()) {
                awaiting.add(client);
            }
        }
        return awaiting;
    }
}
