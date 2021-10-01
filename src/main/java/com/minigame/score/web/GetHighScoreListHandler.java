package com.minigame.score.web;

import com.minigame.score.service.ScoreService;
import com.minigame.web.HTTP;
import com.minigame.web.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

public class GetHighScoreListHandler extends RequestHandler {

    private final ScoreService scoreService = ScoreService.Instance;

    @Override
    public String requestMapping() {
        return Constants.HIGH_SCORE_MAPPING;
    }

    @Override
    public boolean isSupportHttpMethod(String method) {
        return HTTP.METHOD_GET.equals(method);
    }

    @Override
    public String handleRequest(HttpExchange exchange, Map<String, String> props) throws Exception {
        getUser(props);
        int levelId = getIntValue(props, Constants.PATH_VAR_LEVEL_ID);
        return scoreService.topScoresForLevel(levelId);
    }
}
