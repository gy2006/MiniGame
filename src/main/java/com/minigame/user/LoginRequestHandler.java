package com.minigame.user;

import com.minigame.web.HTTP;
import com.minigame.web.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

public class LoginRequestHandler extends RequestHandler {

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
    public String handleRequest(HttpExchange exchange, Map<String, String> props) {
        int userId = getIntValue(props, PATH_VAR_USER_ID);
        return userService.login(userId);
    }
}
