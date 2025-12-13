package go.project.server.server.json;

public class GameResponse {
    public int status;
    public String message;

    public final static int STATUS_OK = 0;
    public final static int STATUS_ERROR = 1;

    public final static String MESSAGE_MOVE_OK = "Move accepted";
    public final static String MESSAGE_INVALID_MOVE = "Invalid move";
    public final static String MESSAGE_INTERNAL_ERROR = "Internal error";
    public final static String MESSAGE_MATCH_ENDED = "Match ended";

    public GameResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
