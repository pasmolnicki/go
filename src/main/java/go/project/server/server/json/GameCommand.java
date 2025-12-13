package go.project.server.server.json;


public class GameCommand<T> {
    static public class PayloadMakeMove {
        public String move;
    }

    public String command;
    public String playerid;
    public T payload;
}

