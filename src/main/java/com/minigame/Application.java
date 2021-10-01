package com.minigame;

import com.minigame.score.web.GetHighScoreListHandler;
import com.minigame.score.web.PostScoreHandler;
import com.minigame.user.web.LoginRequestHandler;
import com.minigame.web.UrlMappingHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws IOException {
        LOGGER.log(Level.INFO, ">>>>>>>> Start MiniGame >>>>>>>>");
        startHttpServer();
    }

    private static void startHttpServer() throws IOException {
        final String host = getStringEnvVar("MINIGAME_HOST", "127.0.0.1");
        final int port = getIntEnvVar("MINIGAME_PORT", 8080);
        final int minPoolSize = getIntEnvVar("MINIGAME_MIN_POOL_SIZE", 20);
        final int maxPoolSize = getIntEnvVar("MINIGAME_MAX_POOL_SIZE", 200);
        final int keepAliveTime = getIntEnvVar("MINIGAME_KEEP_ALIVE_TIME", 5000);

        LOGGER.log(Level.INFO, String.format("host: %s", host));
        LOGGER.log(Level.INFO, String.format("port: %s", port));
        LOGGER.log(Level.INFO, String.format("min pool size: %s", minPoolSize));
        LOGGER.log(Level.INFO, String.format("max pool size: %s", maxPoolSize));
        LOGGER.log(Level.INFO, String.format("keep alive time: %s", keepAliveTime));

        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);

        server.setExecutor(new ThreadPoolExecutor(minPoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(minPoolSize),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        LOGGER.log(Level.SEVERE, "Request is rejected");
                    }
                }));

        server.createContext("/", new UrlMappingHandler()
                .registerHandler(new LoginRequestHandler())
                .registerHandler(new PostScoreHandler())
                .registerHandler(new GetHighScoreListHandler())
        );

        server.start();
    }

    private static int getIntEnvVar(String name, int def) {
        return Integer.parseUnsignedInt(System.getenv().getOrDefault(name, "" + def));
    }

    private static String getStringEnvVar(String name, String def) {
        return System.getenv().getOrDefault(name, def);
    }
}
