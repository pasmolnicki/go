package go.project.server.server;

import java.io.PrintWriter;
import java.net.Socket;

import go.project.server.server.json.JsonFmt;

public class ClientHandler implements Runnable {

    static public enum State {
        CONNECTED,
        WAITING,
        IN_MATCH,
        DISCONNECTED
    }

    private ClientData clientData;
    private PrintWriter out;
    private State state;
    
    public ClientHandler(Socket socket) {
        this.state = State.CONNECTED;
        this.clientData = new ClientData(socket);
    }

    @Override
    public void run() {
        // A client has connected
        try {
            out = new PrintWriter(clientData.getConnection().getOutputStream(), true);
            
            // Send the player id
            out.println(JsonFmt.toJson(clientData));
            setState(State.WAITING);

            // Handle client communication here
            while(isWaitingForMatch()) {
                // Waiting for a match
                Thread.yield();
            }

            // Joined a match, further handling will be done in Match class
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    final public void close() {
        try {
            clientData.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.state = State.DISCONNECTED;
    }

    final public State getState() {
        return state;
    }

    final public ClientData getClientData() {
        return clientData;
    }

    synchronized private void setState(final State newState) {
        this.state = newState;
    }

    final public void join() {
        setState(State.IN_MATCH);
    }

    final public boolean isWaitingForMatch() {
        synchronized(this) {
            return this.state == State.WAITING;
        }
    }
}
