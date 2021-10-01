package com.minigame.score.web;

import com.minigame.score.service.ScoreService;
import com.minigame.util.StringHelper;
import com.minigame.web.HTTP;
import com.minigame.web.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

public class PostScoreHandler extends RequestHandler {

    private final static String PATH_VAR_LEVEL_ID = "levelId";

    private final static String MAPPING = String.format("/{%s}/score", PATH_VAR_LEVEL_ID);

    private final ScoreService scoreService = ScoreService.Instance;

    @Override
    public String requestMapping() {
        return MAPPING;
    }

    @Override
    public boolean isSupportHttpMethod(String method) {
        return HTTP.METHOD_POST.equals(method);
    }

    @Override
    public String handleRequest(HttpExchange exchange) throws Exception {
        int userId = getUser(exchange).getId();
        int levelId = getIntValue(exchange, PATH_VAR_LEVEL_ID);
        int score = getScore(exchange);
        scoreService.addScoreToLevel(levelId, userId, score);
        return StringHelper.EMPTY_STRING;
    }

    private int getScore(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return Integer.parseUnsignedInt(StringHelper.toString(is));
    }
}
