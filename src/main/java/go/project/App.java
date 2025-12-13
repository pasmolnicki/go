package go.project;

import go.project.server.server.MockClient;
import go.project.server.server.Server;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if (args.length > 0 && args[0].equals("server")) {
            try {
                Server server = new Server();
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args.length > 0 && args[0].equals("client")) {
            MockClient client = new MockClient();
            try {
                client.connect();
                client.receive();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please specify 'server' or 'client' as an argument.");
        }
    }
}
