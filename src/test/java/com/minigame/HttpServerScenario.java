package com.minigame;

import com.minigame.score.web.GetHighScoreListHandler;
import com.minigame.score.web.PostScoreHandler;
import com.minigame.user.web.LoginRequestHandler;
import com.minigame.util.StringHelper;
import com.minigame.web.HTTP;
import com.minigame.web.UrlMappingHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class HttpServerScenario {

    protected static class Response {

        public int code;

        public String body;
    }

    private static final InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);

    private static HttpServer server;

    @BeforeClass
    public static void startServer() throws IOException {
        server = HttpServer.create(address, 0);
        server.createContext("/", new UrlMappingHandler()
                .registerHandler(new LoginRequestHandler())
                .registerHandler(new PostScoreHandler())
                .registerHandler(new GetHighScoreListHandler())
        );
        new Thread(() -> server.start()).start();
    }

    @AfterClass
    public static void stopServer() {
        server.stop(0);
    }

    protected Response httpGet(String path) throws IOException {
        URL url = getURL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return getResponseData(conn);
    }

    protected Response httpPost(String path, String body) throws IOException {
        URL url = getURL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        return getResponseData(conn);
    }

    private static Response getResponseData(HttpURLConnection conn) throws IOException {
        Response r = new Response();
        r.code = conn.getResponseCode();
        try (InputStream is = conn.getResponseCode() == HTTP.STATUS_CODE_200 ? conn.getInputStream() : conn.getErrorStream()) {
            r.body = StringHelper.toString(is);
        }
        return r;
    }

    private static URL getURL(String path) throws MalformedURLException {
        return new URL(String.format("http://%s:%d%s", address.getHostName(), address.getPort(), path));
    }
}
