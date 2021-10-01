package com.minigame.user;

import com.minigame.HttpServerScenario;
import com.minigame.web.HTTP;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class UserLoginTest extends HttpServerScenario {

    private final UserService userService = UserService.Instance;

    private final String loginUrl = "/1001/login";

    @Test
    public void should_get_session_key_after_login() throws IOException {
        Response firstSessionKey = httpGet(loginUrl);
        Assert.assertEquals(HTTP.STATUS_CODE_200, firstSessionKey.code);
        Assert.assertTrue(userService.getUser(firstSessionKey.body).isPresent());
    }

    @Test
    public void should_have_diff_session_key_within_expire_time() throws IOException {
        Response firstSessionKey = httpGet(loginUrl);
        Assert.assertEquals(HTTP.STATUS_CODE_200, firstSessionKey.code);
        Assert.assertTrue(userService.getUser(firstSessionKey.body).isPresent());

        Response secondSessionKey = httpGet(loginUrl);
        Assert.assertNotNull(secondSessionKey);
        Assert.assertFalse(userService.getUser(firstSessionKey.body).isPresent());
        Assert.assertTrue(userService.getUser(secondSessionKey.body).isPresent());

        Assert.assertNotEquals(firstSessionKey, secondSessionKey);
    }
}
