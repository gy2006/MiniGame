package com.minigame.score;

public interface ScoreService {

    ScoreService Instance = new ScoreServiceImpl();

    void addScoreToLevel(int userId, int score, int levelId);

    String highScoresForLevel(int levelId);
}
