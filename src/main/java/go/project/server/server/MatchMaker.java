package go.project.server.server;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatchMaker implements Runnable {
    private ClientManager clientManager;
    private MatchManager matchManager;
    private ExecutorService matchPool = Executors.newCachedThreadPool();

    public MatchMaker(ClientManager clientManager, MatchManager matchManager) {
        this.clientManager = clientManager;
        this.matchManager = matchManager;
    }

    @Override
    public void run() {
        while (true) {
            tryCreateMatch();
            tryCleanup();
            Thread.yield();
        }
    }

    /**
     * Cleans up completed matches.
     */
    private void tryCleanup() {
        matchManager.cleanupCompletedMatches();
    }

    /**
     * Attempts to create matches from awaiting clients.
     */
    private void tryCreateMatch() {
        Vector<ClientHandler> awaitingClients = clientManager.awaitingClients();
        while (awaitingClients.size() >= 2) {
            ClientHandler cl1 = awaitingClients.remove(0);
            ClientHandler cl2 = awaitingClients.remove(0);

            cl1.join();
            cl2.join();

            Match newMatch = matchManager.createMatch(cl1.getClientData(), cl2.getClientData());
            matchPool.execute(newMatch);
        }
    }
}
