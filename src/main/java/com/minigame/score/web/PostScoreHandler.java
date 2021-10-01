package com.minigame.score.web;

import com.minigame.score.service.ScoreService;
import com.minigame.util.StringHelper;
import com.minigame.web.HTTP;
import com.minigame.web.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.minigame.score.web.Constants.PATH_VAR_LEVEL_ID;

public class PostScoreHandler extends RequestHandler {

    private final ScoreService scoreService = ScoreService.Instance;

    @Override
    public String requestMapping() {
        return Constants.POST_SCORE_MAPPING;
    }

    @Override
    public boolean isSupportHttpMethod(String method) {
        return HTTP.METHOD_POST.equals(method);
    }

    @Override
    public String handleRequest(HttpExchange exchange, Map<String, String> props) throws Exception {
        int userId = getUser(props).getId();
        int levelId = getIntValue(props, PATH_VAR_LEVEL_ID);
        int score = getScore(exchange);
        scoreService.addScoreToLevel(levelId, userId, score);
        return StringHelper.EMPTY_STRING;
    }

    private int getScore(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            return Integer.parseUnsignedInt(StringHelper.toString(is));
        } catch (NumberFormatException e) {
            throw new RuntimeException("invalid score number");
        }
    }
}
