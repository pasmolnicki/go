package go.project.server.server;

import java.io.PrintWriter;
import java.net.Socket;

import go.project.server.server.json.Connection;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
    private ClientData clientData;
    private PrintWriter out;
    private boolean waitingForMatch = true;
    
    public ClientHandler(Socket socket) {
        this.clientData = new ClientData(socket);
    }

    @Override
    public void run() {
        // A client has connected
        try {
            out = new PrintWriter(clientData.getConnection().getOutputStream(), true);
            
            // Send the player id
            ObjectMapper mapper = new ObjectMapper();
            String connectionJson = mapper.writeValueAsString(
                new Connection(this.clientData.getClientId()));
            out.println(connectionJson);

            // Handle client communication here
            while(isWaitingForMatch()) {
                // Waiting for a match
                Thread.yield();
            }

            // Joined a match, further handling will be done in Match class
        } catch (Exception e) {
            e.printStackTrace();
            join();

            // Handle disconnection
            try {
                clientData.getConnection().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    final public ClientData getClientData() {
        return clientData;
    }

    final public void join() {
        synchronized(this) {
            waitingForMatch = false;
        }
    }

    final public boolean isWaitingForMatch() {
        synchronized(this) {
            return waitingForMatch;
        }
    }
}
