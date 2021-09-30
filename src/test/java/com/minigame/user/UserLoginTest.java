package com.minigame.user;

import com.minigame.HttpServerScenario;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class UserLoginTest extends HttpServerScenario {

    private final UserService userService = UserService.Instance;

    private final String loginUrl = "/1/login";

    @Test
    public void should_get_session_key_after_login() throws IOException {
        String firstSessionKey = doGet(loginUrl);
        Assert.assertNotNull(firstSessionKey);
        Assert.assertTrue(userService.isLoggedIn(firstSessionKey));
    }

    @Test
    public void should_have_diff_session_key_within_expire_time() throws IOException {
        String firstSessionKey = doGet(loginUrl);
        Assert.assertNotNull(firstSessionKey);
        Assert.assertTrue(userService.isLoggedIn(firstSessionKey));

        String secondSessionKey = doGet(loginUrl);
        Assert.assertNotNull(secondSessionKey);
        Assert.assertFalse(userService.isLoggedIn(firstSessionKey));
        Assert.assertTrue(userService.isLoggedIn(secondSessionKey));

        Assert.assertNotEquals(firstSessionKey, secondSessionKey);
    }
}
