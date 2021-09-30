package com.minigame.user;

import com.minigame.web.HTTP;
import com.minigame.web.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

public class LoginRequestHandler implements RequestHandler {

    private final static String PATH_VAR_USER_ID = "userid";

    private final static String MAPPING = String.format("/{%s}/login", PATH_VAR_USER_ID);

    private final UserService userService = UserService.Instance;

    @Override
    public String requestMapping() {
        return MAPPING;
    }

    @Override
    public boolean isSupportHttpMethod(String method) {
        return method.equals(HTTP.METHOD_GET);
    }

    @Override
    public String handleRequest(HttpExchange exchange) {
        Object userIdAtt = exchange.getAttribute(PATH_VAR_USER_ID);
        int userId = Integer.parseUnsignedInt(userIdAtt.toString());
        return userService.login(userId);
    }
}
