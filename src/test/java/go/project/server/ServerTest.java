package go.project.server;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import go.project.server.server.MockClient;
import go.project.server.server.Server;

public class ServerTest {
    
    private Server server;
    
    ServerTest() {
        try {
            server = new Server();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMockClientConnection() {
        try {
            MockClient client = new MockClient();
            client.connect();
            String resp = client.receive();
            if (resp == null || resp.isEmpty()) {
                assertTrue("MockClient did not receive any data from server", false);
            } else {
                System.out.println("[ServerTest] Received from server: " + resp);
                assertTrue("MockClient successfully connected to server", true);
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("MockClient failed to connect to server", false);
        }
    }
}
