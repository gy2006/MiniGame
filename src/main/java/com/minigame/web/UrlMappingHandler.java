package com.minigame.web;

import com.minigame.util.StringHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UrlMappingHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(UrlMappingHandler.class.getName());

    private final Map<UrlPath, RequestHandler> handlerMap = new HashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            for (Map.Entry<UrlPath, RequestHandler> entry : handlerMap.entrySet()) {
                UrlPath urlPath = entry.getKey();
                RequestHandler handler = entry.getValue();

                if (urlPath.isMatch(exchange) && handler.isSupportHttpMethod(exchange.getRequestMethod())) {
                    Map<String, String> props = urlPath.parsePathVarAndParameter(exchange);
                    String response = handler.handleRequest(exchange, props);
                    writeMessageToResponse(exchange, response, HTTP.STATUS_CODE_200);
                    return;
                }
            }

            throw new RuntimeException("The request is not supported");
        } catch (Throwable e) {
            String err = e.getMessage();
            writeMessageToResponse(exchange, err, HTTP.STATUS_CODE_400);
        }
    }

    public UrlMappingHandler registerHandler(RequestHandler handler) {
        UrlPath urlPath = new UrlPath(handler.requestMapping());
        handlerMap.put(urlPath, handler);
        LOGGER.log(Level.INFO, String.format("url %s registered", urlPath.getRawPath()));
        return this;
    }

    private void writeMessageToResponse(HttpExchange exchange, String message, int httpCode) throws IOException {
        int length = StringHelper.isNullOrEmpty(message) ? -1 : message.length();
        exchange.sendResponseHeaders(httpCode, length);

        if (length > 0) {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(message.getBytes());
            }
        }
    }
}
