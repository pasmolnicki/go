package go.project.server.game.base;


// Keeps the config constants for the server, client, and game
final public class Config {
    private Config() {
        // prevent instantiation
    }

    // Server config
    public static final int PORT = 8080;
    public static final int MAX_CLIENTS = 10;

    // Client config
    

    // Game config
    public static final int DEFAULT_BOARD_SIZE = 19;
}
