package com.minigame.score.service;

public interface ScoreService {

    ScoreService Instance = new ScoreServiceImpl();

    int getScore(int levelId, int userId);

    void addScoreToLevel(int levelId, int userId, int score);

    String topScoresForLevel(int levelId);

    void reset();
}
