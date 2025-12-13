package go.project.server.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import go.project.server.game.base.Config;

public class MockClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Connects to the server using the port defined in Config.
     */
    public void connect() throws IOException {
        this.socket = new Socket("localhost", Config.PORT);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("[MockClient] Connected to server at localhost:" + Config.PORT);
    }

    /**
     * Sends a raw string message (JSON command) to the server.
     */
    public void send(String message) {
        if (out != null) {
            System.out.println("[MockClient] Sending: " + message);
            out.println(message);
        } else {
            System.err.println("[MockClient] Not connected. Cannot send message.");
        }
    }

    /**
     * Blocks until a message is received from the server, then prints and returns it.
     * @return The received message line, or null if connection is closed/error.
     */
    public String receive() {
        try {
            String line = in.readLine();
            if (line != null) {
                System.out.println("[MockClient] Received: " + line);
            } else {
                System.out.println("[MockClient] Server closed connection.");
            }
            return line;
        } catch (IOException e) {
            System.err.println("[MockClient] Error reading from server: " + e.getMessage());
            return null;
        }
    }

    /**
     * Closes the connection.
     */
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("[MockClient] Connection closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}