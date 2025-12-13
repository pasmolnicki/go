package go.project.server;

import static org.junit.Assert.fail;
import org.junit.Test;

import go.project.server.server.Logger;
import go.project.server.server.Server;
import go.project.server.server.json.JsonFmt;
import go.project.server.server.json.PlayerTurn;

public class ServerTest {

    static final boolean VERBOSE = true;

    static void log(String msg) {
        if (VERBOSE) {
            System.out.println("[ServerTest] " + msg);
        }
    }


    /**
     * Tests a basic connection from a MockClient to the Server.
     * Verifies that:
     * a) The server starts without exceptions.
     * b) The MockClient can connect to the server.
     * c) The MockClient receives some data from the server upon connection.
     */
    @Test
    public void testMockClientConnection() {
        try {
            // Pass true to see verbose server logs
            Server server = new Server(Logger.LEVEL_ERROR);
            server.async();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Server failed to start: " + e.getMessage());
            return;
        }

        try {
            MockClient client = new MockClient();
            client.connect();
            String resp = client.receive();
            if (resp == null || resp.isEmpty()) {
                fail("MockClient did not receive any data from server");
            } else {
                log("Received from server: " + resp);
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("MockClient failed to connect to server");
        }
    }

    /**
     * Tests that two MockClients can connect to the Server and get matched into a game.
     * Verifies that:
     * a) Both clients receive their player side assignments (BLACK/WHITE).
     * b) The sides are opposite for the two clients.
     */
    @Test
    public void testMatchMaking() {
        Server server;
        try {
            // Pass LEVEL_ALL to see verbose server logs
            server = new Server(Logger.LEVEL_ALL);
            server.async();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Server failed to start: " + e.getMessage());
            return;
        }        

        // Connect two mock clients and verify they get matched
        try {
            MockClient client1 = new MockClient();
            client1.connect();
            String resp1 = client1.receive();
            log("Client1 received: " + resp1);
            MockClient client2 = new MockClient();
            client2.connect();
            String resp2 = client2.receive();
            log("Client2 received: " + resp2);

            String side1 = client1.receive();
            String side2 = client2.receive();
            log("Client1 side: " + side1);
            log("Client2 side: " + side2);

            // Make sure the sides are opposite, and both are valid json format
            if (side1 == null || side2 == null || side1.isEmpty() || side2.isEmpty()) {
                fail("One of the clients did not receive side assignment");
            }

            PlayerTurn pt1 = JsonFmt.fromJson(side2, PlayerTurn.class);
            PlayerTurn pt2 = JsonFmt.fromJson(side1, PlayerTurn.class);
            
            if (pt1.getSide().equals(pt2.getSide())) {
                fail("Both clients received the same side assignment");
            }

            client1.close();
            client2.close();
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
            fail("MockClients failed during match making test");
        }
    }

    @Test
    public void testMatchPlay() {
        
    }
}
