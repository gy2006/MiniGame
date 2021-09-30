package com.minigame;

import com.minigame.user.LoginRequestHandler;
import com.minigame.web.UrlMappingHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) throws IOException {
        startHttpServer();
    }

    private static void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        server.setExecutor(new ThreadPoolExecutor(50, 200, 5000, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("Request is rejected");
            }
        }));

        server.createContext("/", new UrlMappingHandler()
                .registerHandler(new LoginRequestHandler())
        );

        server.start();
    }
}
