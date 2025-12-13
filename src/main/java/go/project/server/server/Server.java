package go.project.server.server;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

import go.project.server.game.base.Config;

import java.util.concurrent.ExecutorService;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService clientPool;
    private Thread matchMakerThread;
    private MatchManager matchManager = new MatchManager();
    private ClientManager clientManager = new ClientManager();

    public Server() throws Exception {
        serverSocket = new ServerSocket(Config.PORT);
    }

    // Runs infinitely to accept client connections
    public void start() throws Exception {
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        
        this.matchMakerThread = new Thread(new MatchMaker(clientManager, matchManager));
        this.matchMakerThread.start();

        this.clientPool = Executors.newFixedThreadPool(Config.MAX_CLIENTS);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientManager.addClient(clientHandler);
            clientPool.execute(clientHandler);
        }
    }
}
