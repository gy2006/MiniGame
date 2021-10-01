package com.minigame.score.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Score {

    private final Integer level;

    private final Map<Integer, Integer> userScoreMap = new HashMap<>();

    public Score(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public Optional<Integer> get(int userId) {
        return Optional.ofNullable(userScoreMap.get(userId));
    }

    public void plus(int userId, int score) {
        Integer currentScore = userScoreMap.get(userId);
        if (currentScore == null) {
            currentScore = 0;
        }
        userScoreMap.put(userId, currentScore + score);
    }
}
