package com.minigame.score.service;

import com.minigame.score.domain.TopScoreList;
import com.minigame.score.domain.Score;
import com.minigame.user.service.UserService;
import com.minigame.util.ResourceHelper;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreServiceImpl implements ScoreService {

    private static final int TOP_SCORE_LIST_CAPACITY = 15;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private final Map<Integer, Score> db;

    private final Map<Integer, TopScoreList> topScores;

    private final List<Integer> levels;

    ScoreServiceImpl() {
        levels = loadLevel();
        db = new HashMap<>(levels.size());
        topScores = new HashMap<>(levels.size());

        for (Integer level : levels) {
            db.put(level, new Score(level));
            topScores.put(level, new TopScoreList(TOP_SCORE_LIST_CAPACITY));
        }
    }

    @Override
    public int getScore(int levelId, int userId) {
        Score s = getLevelScore(levelId);
        Optional<Integer> score = s.get(userId);
        return score.orElse(0);
    }

    @Override
    public void addScoreToLevel(int levelId, int userId, int score) {
        Score s = getLevelScore(levelId);
        TopScoreList top = topScores.get(levelId);
        synchronized (s.getLevel()) {
            s.plus(userId, score);
            top.plus(userId, score);
        }
    }

    @Override
    public String topScoresForLevel(int levelId) {
        Score score = getLevelScore(levelId);
        TopScoreList top = topScores.get(score.getLevel());
        return top.toString();
    }

    @Override
    public void reset() {
        db.clear();
        topScores.clear();

        for (Integer level : levels) {
            db.put(level, new Score(level));
            topScores.put(level, new TopScoreList(TOP_SCORE_LIST_CAPACITY));
        }
    }

    private Score getLevelScore(int levelId) {
        Score s = db.get(levelId);
        if (s == null) {
            throw new RuntimeException("invalid level");
        }
        return s;
    }

    private List<Integer> loadLevel() {
        List<Integer> levels = new LinkedList<>();

        ResourceHelper.load("levels.txt", (line) -> {
            Integer level = Integer.parseUnsignedInt(line.trim());
            levels.add(level);
        });

        LOGGER.log(Level.INFO, String.format("%d levels loaded", levels.size()));
        return levels;
    }
}
