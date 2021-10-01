package com.minigame.score;

import com.minigame.HttpServerScenario;
import com.minigame.score.service.ScoreService;
import com.minigame.util.StringHelper;
import com.minigame.web.HTTP;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class GetHighScoreTest extends HttpServerScenario {

    private final ScoreService service = ScoreService.Instance;

    @After
    public void cleanUp() {
        service.reset();
    }

    @Test
    public void shouldGetHttp400IfSessionKeyIsMissing() throws IOException {
        Response newScore = httpGet("/1/highscorelist");
        Assert.assertEquals(HTTP.STATUS_CODE_400, newScore.code);
        Assert.assertEquals("session key is required", newScore.body);
    }

    @Test
    public void shouldGetHttp400IfSessionKeyIsInvalid() throws IOException {
        Response newScore = httpGet("/1/highscorelist?sessionkey=100001");
        Assert.assertEquals(HTTP.STATUS_CODE_400, newScore.code);
        Assert.assertEquals("invalid session key", newScore.body);
    }

    @Test
    public void shouldGetHtt400IfLevelIdInInvalid() throws IOException {
        Response sessionKey = httpGet("/1001/login");
        Assert.assertEquals(HTTP.STATUS_CODE_200, sessionKey.code);

        Response newScore = httpGet("/100/highscorelist?sessionkey=" + sessionKey.body);
        Assert.assertEquals(HTTP.STATUS_CODE_400, newScore.code);
        Assert.assertEquals("invalid level", newScore.body);
    }

    @Test
    public void shouldGetEmptyResponseNoScoreForLevelAndUser() throws IOException {
        Response sessionKey = httpGet("/1001/login");
        Assert.assertEquals(HTTP.STATUS_CODE_200, sessionKey.code);

        Response newScore = httpGet("/1/highscorelist?sessionkey=" + sessionKey.body);
        Assert.assertEquals(HTTP.STATUS_CODE_200, newScore.code);
        Assert.assertEquals(StringHelper.EMPTY_STRING, newScore.body);
    }

    @Test
    public void shouldGetCorrectlyResponse() throws IOException {
        // init: login user 1001 and 1002
        Response sessionKeyForUser1 = httpGet("/1001/login");
        Assert.assertEquals(HTTP.STATUS_CODE_200, sessionKeyForUser1.code);

        Response sessionKeyForUser2 = httpGet("/1002/login");
        Assert.assertEquals(HTTP.STATUS_CODE_200, sessionKeyForUser2.code);

        // when: post scores
        httpPost("/1/score?sessionkey=" + sessionKeyForUser1.body, "5");
        httpPost("/1/score?sessionkey=" + sessionKeyForUser2.body, "5");

        httpPost("/2/score?sessionkey=" + sessionKeyForUser2.body, "10");
        httpPost("/2/score?sessionkey=" + sessionKeyForUser1.body, "1");

        // then:
        Response listForLevel1 = httpGet("/1/highscorelist?sessionkey=" + sessionKeyForUser1.body);
        Assert.assertEquals(HTTP.STATUS_CODE_200, listForLevel1.code);
        Assert.assertEquals("1001=5,1002=5", listForLevel1.body);

        Response listForLevel2 = httpGet("/2/highscorelist?sessionkey=" + sessionKeyForUser2.body);
        Assert.assertEquals(HTTP.STATUS_CODE_200, listForLevel2.code);
        Assert.assertEquals("1002=10,1001=1", listForLevel2.body);
    }
}
