package com.minigame.web;

import com.sun.net.httpserver.HttpExchange;

public interface RequestHandler {

    String requestMapping();

    boolean isSupportHttpMethod(String method);

    String handleRequest(HttpExchange exchange) throws Exception;
}
