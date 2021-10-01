package com.minigame.web;

import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class UrlPathTest {

    @Test
    public void should_get_regex_path() {
        UrlPath pathWithVars = new UrlPath("/{id}/user/{name}");
        Assert.assertEquals("/[a-zA-Z0-9]+/user/[a-zA-Z0-9]+", pathWithVars.getRegexPath());
        Assert.assertEquals(2, pathWithVars.getSizeOfPathVar());

        UrlPath pathWithoutVars = new UrlPath("/user");
        Assert.assertEquals("/user", pathWithoutVars.getRegexPath());
    }

    @Test
    public void should_match_lookup_path() {
        Assert.assertTrue(new UrlPath("/{id}/user/{name}").isMatch(new DummyHttpExchange("/12/user/admin")));
        Assert.assertTrue(new UrlPath("/user/{name}").isMatch(new DummyHttpExchange("/user/admin")));
        Assert.assertTrue(new UrlPath("/{id}/login").isMatch(new DummyHttpExchange("/666/login")));
        Assert.assertTrue(new UrlPath("/{name}/{id}").isMatch(new DummyHttpExchange("/admin/123")));
        Assert.assertTrue(new UrlPath("/health").isMatch(new DummyHttpExchange("/health")));

        Assert.assertFalse(new UrlPath("/user/{name}").isMatch(new DummyHttpExchange("/user/admin/ext")));
        Assert.assertFalse(new UrlPath("/{name}").isMatch(new DummyHttpExchange("/admin/1/2")));
        Assert.assertFalse(new UrlPath("/{name}").isMatch(new DummyHttpExchange("/admin/1/2")));
        Assert.assertFalse(new UrlPath("/user").isMatch(new DummyHttpExchange("/user1")));

        Assert.assertTrue(new UrlPath("/{id}/user/{name}").isMatch(new DummyHttpExchange("/12/user/admin?key=1")));
        Assert.assertTrue(new UrlPath("/{id}/user/{name}").isMatch(new DummyHttpExchange("/12/user/admin?key=1&val=12")));
    }

    @Test
    public void should_get_value_of_path_variable() {
        UrlPath path = new UrlPath("/{userid}/user/{name}");

        HttpExchange exchange = new DummyHttpExchange("/12/user/admin");
        Map<String, String> props = path.parsePathVarAndParameter(exchange);

        Assert.assertEquals("12", props.get("userid"));
        Assert.assertEquals("admin", props.get("name"));
        Assert.assertNull(props.get("not_exist"));
    }

    @Test
    public void should_get_value_of_url_params() {
        UrlPath path = new UrlPath("/{userid}/user/{name}");

        HttpExchange exchange = new DummyHttpExchange("/12/user/admin?session=123&key=abc&uuid=");
        Map<String, String> props = path.parsePathVarAndParameter(exchange);

        Assert.assertEquals("123", props.get("session"));
        Assert.assertEquals("abc", props.get("key"));
        Assert.assertNull(props.get("uuid"));
    }
}
