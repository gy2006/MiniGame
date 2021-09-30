package com.minigame.score;

import com.minigame.user.UserService;
import com.minigame.util.ResourceHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreServiceImpl implements ScoreService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    private final Map<Integer, Map<Integer, Integer>> scoreDB = new ConcurrentHashMap<>();

    ScoreServiceImpl() {
        ResourceHelper.load("levels.txt", (line) -> {
            Integer level = Integer.parseUnsignedInt(line.trim());
            scoreDB.put(level, new ConcurrentHashMap<>());
        });

        logger.log(Level.INFO, String.format("%d levels loaded", scoreDB.size()));
    }

    @Override
    public void addScoreToLevel(int userId, int score, int levelId) {

    }

    @Override
    public String highScoresForLevel(int levelId) {
        return null;
    }
}
