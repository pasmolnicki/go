package go.project.server.server;

import java.util.concurrent.ExecutorService;

import go.project.common.Config;

public class ClientPool {
    private ClientManager clientManager;
    private ExecutorService clientPool;
    private boolean isRunning = true;

    public ClientPool(ClientManager clientManager) {
        this.clientManager = clientManager;
        this.clientPool = java.util.concurrent.Executors.newFixedThreadPool(Config.MAX_CLIENTS);
    }

    /**
     * Adds a new client to the pool and starts its handler.
     */
    synchronized public void addClient(final ClientHandler clientHandler) {
        if (!isRunning) {
            throw new IllegalStateException("ClientPool is not running");
        }
        clientManager.addClient(clientHandler);
        clientPool.execute(clientHandler);
    }

    /**
     * Removes a client from the pool by clientId.
     */
    synchronized public void removeClient(final String clientId) {
        if (!isRunning) {
            throw new IllegalStateException("ClientPool is not running");
        }
        clientManager.removeClient(clientId);
    }

    /**
     * Checks if the client pool is running.
     */
    synchronized public boolean isShutdown() {
        return !isRunning;
    }

    /**
     * Shuts down the client pool and stops all client handlers.
     */
    synchronized public void shutdown() {
        if (!isRunning) {
            return;
        }

        isRunning = false;

        for (ClientHandler client : clientManager.awaitingClients()) {
            client.close();
        }

        clientPool.shutdownNow();
    }
}
