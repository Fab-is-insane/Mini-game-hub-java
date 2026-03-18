package utils;

import java.util.HashMap;
import java.util.Map;

public class ScoreManager {

    private static ScoreManager instance;

    private Map<String, Integer> currentScores = new HashMap<>();
    private Map<String, Integer> topScores     = new HashMap<>();
    private Map<String, Integer> gamesPlayed   = new HashMap<>();

    private ScoreManager() {}

    public static ScoreManager getInstance() {
        if (instance == null) instance = new ScoreManager();
        return instance;
    }

    public void addScore(String game, int points) {
        int cur = currentScores.getOrDefault(game, 0) + points;
        currentScores.put(game, cur);
        if (cur > topScores.getOrDefault(game, 0)) topScores.put(game, cur);
    }

    public void incrementGamesPlayed(String game) {
        gamesPlayed.put(game, gamesPlayed.getOrDefault(game, 0) + 1);
    }

    public int getCurrentScore(String game) { return currentScores.getOrDefault(game, 0); }
    public int getTopScore(String game)     { return topScores.getOrDefault(game, 0); }
    public int getGamesPlayed(String game)  { return gamesPlayed.getOrDefault(game, 0); }

    public void resetCurrentScore(String game) { currentScores.put(game, 0); }

    public int getTotalScore() {
        return currentScores.values().stream().mapToInt(Integer::intValue).sum();
    }
}
