package com.minigame.score.web;

public abstract class Constants {

    final static String PATH_VAR_LEVEL_ID = "levelId";

    final static String POST_SCORE_MAPPING = String.format("/{%s}/score", PATH_VAR_LEVEL_ID);

    final static String HIGH_SCORE_MAPPING = String.format("/{%s}/highscorelist", PATH_VAR_LEVEL_ID);
}
