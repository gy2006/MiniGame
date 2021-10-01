package com.minigame.score;

import com.minigame.score.domain.TopScoreList;
import com.minigame.score.domain.Score;
import com.minigame.user.UserService;
import com.minigame.util.ResourceHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreServiceImpl implements ScoreService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    private final Map<Integer, Score> db;

    private final Map<Integer, TopScoreList> topScores;

    ScoreServiceImpl() {
        List<Integer> levels = loadLevel();

        db = new HashMap<>(levels.size());
        topScores = new HashMap<>(levels.size());

        for (Integer level : levels) {
            db.put(level, new Score(level));
            topScores.put(level, new TopScoreList(15));
        }
    }

    @Override
    public void addScoreToLevel(int levelId, int userId, int score) {
        Score s = db.get(levelId);
        TopScoreList top = topScores.get(levelId);

        synchronized (s.getLevel()) {
            s.plus(userId, score);
            top.plus(userId, score);
        }
    }

    @Override
    public String topScoresForLevel(int levelId) {
        return null;
    }

    private List<Integer> loadLevel() {
        List<Integer> levels = new LinkedList<>();

        ResourceHelper.load("levels.txt", (line) -> {
            Integer level = Integer.parseUnsignedInt(line.trim());
            levels.add(level);
        });

        logger.log(Level.INFO, String.format("%d levels loaded", levels.size()));
        return levels;
    }
}
