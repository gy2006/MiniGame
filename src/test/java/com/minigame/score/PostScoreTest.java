package com.minigame.score;

import com.minigame.HttpServerScenario;
import com.minigame.score.service.ScoreService;
import com.minigame.util.StringHelper;
import com.minigame.web.HTTP;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PostScoreTest extends HttpServerScenario {

    private final ScoreService service = ScoreService.Instance;

    @After
    public void cleanUp() {
        service.reset();
    }

    @Test
    public void shouldGetHttp400IfSessionKeyIsMissing() throws IOException {
        Response newScore = httpPost("/1/score", "1000");
        Assert.assertEquals(HTTP.STATUS_CODE_400, newScore.code);
        Assert.assertEquals("session key is required", newScore.body);
    }

    @Test
    public void shouldGetHttp400IfSessionKeyIsInvalid() throws IOException {
        Response newScore = httpPost("/1/score?sessionkey=100001", "1000");
        Assert.assertEquals(HTTP.STATUS_CODE_400, newScore.code);
        Assert.assertEquals("invalid session key", newScore.body);
    }

    @Test
    public void shouldGetHtt400IfLevelIdInInvalid() throws IOException {
        Response sessionKey = httpGet("/1001/login");
        Assert.assertEquals(HTTP.STATUS_CODE_200, sessionKey.code);

        Response newScore = httpPost("/100/score?sessionkey=" + sessionKey.body, "1000");
        Assert.assertEquals(HTTP.STATUS_CODE_400, newScore.code);
        Assert.assertEquals("invalid level", newScore.body);
    }

    @Test
    public void shouldGetHtt400IfScoreIsEmpty() throws IOException {
        Response sessionKey = httpGet("/1001/login");
        Assert.assertEquals(HTTP.STATUS_CODE_200, sessionKey.code);

        Response newScore = httpPost("/1/score?sessionkey=" + sessionKey.body, StringHelper.EMPTY_STRING);
        Assert.assertEquals(HTTP.STATUS_CODE_400, newScore.code);
        Assert.assertEquals("invalid score number", newScore.body);
    }

    @Test
    public void shouldPostNewScore() throws IOException {
        // init: login
        Response sessionKey = httpGet("/1001/login");
        Assert.assertEquals(HTTP.STATUS_CODE_200, sessionKey.code);

        // when: post new score for level 1
        Response newScore = httpPost("/1/score?sessionkey=" + sessionKey.body, "1000");
        Assert.assertEquals(HTTP.STATUS_CODE_200, newScore.code);
        Assert.assertEquals(StringHelper.EMPTY_STRING, newScore.body);

        // then:
        int score = service.getScore(1, 1001);
        Assert.assertEquals(1000, score);

        // when: pose score to level 1
        newScore = httpPost("/1/score?sessionkey=" + sessionKey.body, "300");
        Assert.assertEquals(HTTP.STATUS_CODE_200, newScore.code);
        Assert.assertEquals(StringHelper.EMPTY_STRING, newScore.body);

        // then:
        score = service.getScore(1, 1001);
        Assert.assertEquals(1300, score);

        // when: pose new score to level 2
        newScore = httpPost("/2/score?sessionkey=" + sessionKey.body, "100");
        Assert.assertEquals(HTTP.STATUS_CODE_200, newScore.code);
        Assert.assertEquals(StringHelper.EMPTY_STRING, newScore.body);

        score = service.getScore(2, 1001);
        Assert.assertEquals(100, score);
    }
}
