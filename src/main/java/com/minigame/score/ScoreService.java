package com.minigame.score;

public interface ScoreService {

    ScoreService Instance = new ScoreServiceImpl();

    void addScoreToLevel( int levelId, int userId, int score);

    String highScoresForLevel(int levelId);
}
