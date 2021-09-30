package com.minigame;

import com.minigame.user.LoginRequestHandler;
import com.minigame.util.StringHelper;
import com.minigame.web.UrlMappingHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class HttpServerScenario {

    private static final InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);

    private static HttpServer server;

    @BeforeClass
    public static void startServer() throws IOException {
        server = HttpServer.create(address, 0);
        server.createContext("/", new UrlMappingHandler()
                .registerHandler(new LoginRequestHandler())
        );
        new Thread(() -> server.start()).start();
    }

    @AfterClass
    public static void stopServer() {
        server.stop(0);
    }

    protected String doGet(String path) throws IOException {
        URL url = getURL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        return StringHelper.toString(is);
    }

    protected static URL getURL(String path) throws MalformedURLException {
        return new URL(String.format("http://%s:%d%s", address.getHostName(), address.getPort(), path));
    }
}
