package com.minigame.score.domain;

import java.util.HashMap;
import java.util.Map;

public class LevelScore {

    private final Integer level;

    private final Map<Integer, UserScore> userScoreMap = new HashMap<>();

    public LevelScore(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public UserScore getUserScore(int userId) {
        UserScore us = userScoreMap.get(userId);
        if (us == null) {
            us = new UserScore(userId);
            userScoreMap.put(userId, us);
        }
        return us;
    }
}
