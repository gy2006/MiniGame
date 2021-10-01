package com.minigame.web;

import com.minigame.user.User;
import com.minigame.user.UserService;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import java.util.Optional;

public abstract class RequestHandler {

    public abstract String requestMapping();

    public abstract boolean isSupportHttpMethod(String method);

    public abstract String handleRequest(HttpExchange exchange, Map<String, String> props) throws Exception;

    public int getIntValue(Map<String, String> props, String name) {
        return Integer.parseUnsignedInt(props.get(name));
    }

    public User getUser(Map<String, String> props) {
        Object sessionKey = props.get("sessionkey");
        if (sessionKey == null) {
            throw new RuntimeException("session key is required");
        }

        UserService us = UserService.Instance;
        Optional<User> user = us.getUser(sessionKey.toString());

        if (user.isPresent()) {
            return user.get();
        }

        throw new RuntimeException("invalid session key");
    }
}
