package com.minigame.score;

import com.minigame.score.domain.LevelScore;
import com.minigame.score.domain.UserScore;
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

    private final Map<Integer, LevelScore> db;

    ScoreServiceImpl() {
        List<Integer> levels = loadLevel();

        db = new HashMap<>(levels.size());
        for (Integer level : levels) {
            db.put(level, new LevelScore(level));
        }
    }

    @Override
    public void addScoreToLevel(int levelId, int userId, int score) {
        LevelScore ls = db.get(levelId);
        synchronized (ls.getLevel()) {
            UserScore us = ls.getUserScore(userId);
            us.add(score);
        }
    }

    @Override
    public String highScoresForLevel(int levelId) {
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
