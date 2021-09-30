package com.minigame.score.domain;

public class UserScore {

    private final Integer userId;

    private Integer score = 0;

    public UserScore(Integer userId) {
        this.userId = userId;
    }

    public UserScore(Integer userId, Integer score) {
        this.userId = userId;
        this.score = score;
    }

    public Integer getUserId() {
        return userId;
    }

    public void add(int score) {
        this.score += score;
    }

    public Integer getScore() {
        return score;
    }
}
