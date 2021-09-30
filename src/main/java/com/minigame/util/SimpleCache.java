package com.minigame.util;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Non thread safe cache
 */
public class SimpleCache<K, V> {

    private class Item {

        public V data;

        public Instant expiredAt;

        public Item(V data, Instant expiredAt) {
            this.data = data;
            this.expiredAt = expiredAt;
        }
    }

    private final TemporalUnit timeUnitForTimeout;

    private final int timeout;

    private final Map<K, Item> cache = new WeakHashMap<>();

    public SimpleCache(int timeout, TemporalUnit unit) {
        this.timeout = timeout;
        this.timeUnitForTimeout = unit;
    }

    public void put(K key, V val) {
        cache.put(key, new Item(val, Instant.now().plus(timeout, timeUnitForTimeout)));
    }

    public void evict(K key) {
        cache.remove(key);
    }

    public V get(K key) {
        Item item = cache.get(key);
        if (item == null) {
            return null;
        }

        // expired
        if (item.expiredAt.isBefore(Instant.now())) {
            cache.remove(key);
            return null;
        }

        return item.data;
    }
}
