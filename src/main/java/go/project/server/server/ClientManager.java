package go.project.server.server;

import java.util.HashMap;
import java.util.Vector;

public class ClientManager {
    private HashMap<String, ClientHandler> clients;

    public ClientManager() {
        clients = new HashMap<>();
    }

    // Add a new client
    public final void addClient(final ClientHandler client) {
        clients.put(client.getClientData().getClientId(), client);
    }

    // Remove a client by clientId
    public final void removeClient(final String clientId) {
        clients.remove(clientId);
    }

    // Get all clients waiting for a match
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
