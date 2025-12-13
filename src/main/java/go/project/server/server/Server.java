package go.project.server.server;


import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;

import go.project.server.game.base.Config;

import java.util.concurrent.ExecutorService;

public class Server {

    // Main server socket, the server is listening on
    private ServerSocket serverSocket;

    // Thread pool for handling client connections, that is waiting for 
    // the match to start and send upon connection data
    private ExecutorService clientPool;

    // Thread for the match maker, which pairs clients into matches
    private Thread matchMakerThread;

    // Managers for clients and matches
    private MatchManager matchManager = new MatchManager();
    private ClientManager clientManager = new ClientManager();

    // Server running state
    private boolean isRunning = true;


    /**
     * Initializes the server on the specified port.
     * @throws Exception If the server socket cannot be created (e.g., port is in use).
     */
    public Server() throws Exception {
        serverSocket = new ServerSocket(Config.PORT);
        Logger.getInstance().setLogLevel(Logger.LEVEL_ALL);
    }

    /**
     * Initializes the server on the specified port with a given log level.
     * @param logLevel The logging level to use.
     * @throws Exception If the server socket cannot be created (e.g., port is in use).
     */
    public Server(int logLevel) throws Exception {
        serverSocket = new ServerSocket(Config.PORT);
        Logger.getInstance().setLogLevel(logLevel);
    }

    private void log(String msg) {
        Logger.getInstance().log("Server", msg);
    }

    /**
     * Starts the server and begins accepting client connections.
     * @throws Exception
     */
    public void start() throws Exception {
        log("Server started on port " + serverSocket.getLocalPort());
        
        this.matchMakerThread = new Thread(new MatchMaker(clientManager, matchManager));
        this.matchMakerThread.start();

        this.clientPool = Executors.newFixedThreadPool(Config.MAX_CLIENTS);

        try {
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                log("New client connected: " + clientSocket.getRemoteSocketAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientManager.addClient(clientHandler);
                clientPool.execute(clientHandler);
            } 
        } catch(SocketException se) {
            if (isRunning) {
                throw se; // re-throw if not caused by server stop
            }
        }
    }

    /**
     * Starts the server asynchronously in a new thread.
     */
    public void async() {
        new Thread(() -> {
            try {
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Stops the server and releases resources.
     * @throws Exception
     */
    public void stop() throws Exception {
        synchronized(this) {
            if (!isRunning) {
                return;
            }

            isRunning = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (clientPool != null && !clientPool.isShutdown()) {
                clientPool.shutdownNow();
            }
            if (matchMakerThread != null && matchMakerThread.isAlive()) {
                matchMakerThread.interrupt();
            }
            log("Server stopped.");
        }
    }
}
