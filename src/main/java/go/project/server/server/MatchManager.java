package go.project.server.server;

import java.util.HashMap;

public class MatchManager {
    private HashMap<String, Match> matches;

    public MatchManager() {
        matches = new HashMap<>();
    }

    /**
     * Create a new match between two clients.
     * @return The created match
     */
    public final Match createMatch(final ClientData cl1, final ClientData cl2) {
        Match newMatch = new Match(new ClientData(cl1), new ClientData(cl2));
        matches.put(newMatch.getMatchId(), newMatch);
        return newMatch;
    }

    /**
     * Remove a match by matchId
     */
    public final void removeMatch(final String matchId) {
        matches.remove(matchId);
    }

    /**
     * Cleans up matches that are no longer ongoing.
     */
    public final void cleanupCompletedMatches() {
        matches.values().removeIf(match -> match.getState() != Match.State.ONGOING);
    }
}
