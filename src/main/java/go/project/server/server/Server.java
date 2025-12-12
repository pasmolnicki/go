package go.project.server.server;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

import go.project.server.game.base.Config;

import java.util.concurrent.ExecutorService;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService clientPool;

    public Server(int port) throws Exception {
        serverSocket = new ServerSocket(port);
    }

    // Runs infinitely to accept client connections
    public void start() throws Exception {
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        
        this.clientPool = Executors.newFixedThreadPool(Config.MAX_CLIENTS);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());
            clientPool.execute(new ClientHandler(clientSocket));
        }
    }
}
