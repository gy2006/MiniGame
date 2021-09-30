package com.minigame.util;

import com.minigame.util.SimpleCache;
import org.junit.Assert;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

public class SimpleCacheTest {

    @Test
    public void should_get_value_within_expired_time() throws InterruptedException {
        SimpleCache<String, String> cache = new SimpleCache<>(1, ChronoUnit.SECONDS);
        cache.put("1", "a");

        Thread.sleep(500);
        Assert.assertEquals("a", cache.get("1"));
    }

    @Test
    public void should_not_get_value_after_expired() throws InterruptedException {
        SimpleCache<String, String> cache = new SimpleCache<>(1, ChronoUnit.SECONDS);
        cache.put("1", "a");

        Thread.sleep(1500);
        Assert.assertNull(cache.get("1"));
    }
}
