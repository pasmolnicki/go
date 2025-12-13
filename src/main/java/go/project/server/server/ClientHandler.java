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
    
    /**
     * Initializes a ClientHandler for the given socket connection.
     * @param socket The socket connected to the client.
     */
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
            out.println(JsonFmt.toJson(clientData.data()));
            setState(State.WAITING);

            // wait for a match to be assigned
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

    /**
     * Closes the client connection and updates state.
     */
    final public void close() {
        try {
            clientData.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.state = State.DISCONNECTED;
    }

    /**
     * Gets the current state of the client.
    */ 
    final public State getState() {
        return state;
    }

    /**
     * Gets the ClientData associated with this handler.
     */
    final public ClientData getClientData() {
        return clientData;
    }

    /**
     * Sets the state of the client handler in a thread-safe manner.
     */
    synchronized private void setState(final State newState) {
        this.state = newState;
    }

    /**
     * Marks the client as having joined a match.
     */
    final public void join() {
        setState(State.IN_MATCH);
    }

    /**
     * Checks if the client is waiting for a match (thread-safe).
     * @return true if the client is in WAITING state, false otherwise.
     */
    final public boolean isWaitingForMatch() {
        synchronized(this) {
            return this.state == State.WAITING;
        }
    }
}
