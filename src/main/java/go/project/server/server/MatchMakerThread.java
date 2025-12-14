package go.project.server.server;

public class MatchMakerThread extends Thread {
    private MatchMaker matchMaker;

    public MatchMakerThread(ClientManager clientManager, MatchManager matchManager) {
        this.matchMaker = new MatchMaker(clientManager, matchManager);
    }

    @Override
    public void run() {
        matchMaker.run();
    }

    public void kill() {
        matchMaker.stop();
    }
}
