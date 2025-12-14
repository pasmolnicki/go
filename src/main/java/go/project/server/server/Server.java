package go.project.server.server;


import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import go.project.common.Config;


public class Server {

    // Main server socket, the server is listening on
    private ServerSocket serverSocket;

    // Thread pool for handling client connections, that is waiting for 
    // the match to start and send upon connection data
    private ClientPool clientPool;

    // Thread for the match maker, which pairs clients into matches
    private MatchMakerThread matchMakerThread;

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
     * @param port The port number on which the server will listen.
     * @throws Exception If the server socket cannot be created (e.g., port is in use).
     */
    public Server(int logLevel, int port) throws Exception {
        serverSocket = new ServerSocket(port);
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
        
        this.matchMakerThread = new MatchMakerThread(clientManager, matchManager);
        this.matchMakerThread.start();
        this.clientPool = new ClientPool(clientManager);

        try {
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                log("New client connected: " + clientSocket.getRemoteSocketAddress());
                clientPool.addClient(new ClientHandler(clientSocket));
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

            log("Shutting down server...");
            isRunning = false;
            if (clientPool != null && !clientPool.isShutdown()) {
                clientPool.shutdown();
            }
            if (matchMakerThread != null && matchMakerThread.isAlive()) {
                matchMakerThread.kill();
                matchMakerThread.join();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            log("Server stopped.");
        }
    }
}
