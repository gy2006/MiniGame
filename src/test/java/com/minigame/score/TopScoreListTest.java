package com.minigame.score;

import com.minigame.score.domain.TopScoreList;
import org.junit.Assert;
import org.junit.Test;

public class TopScoreListTest {

    @Test
    public void shouldHandleListCorrectly() {
        TopScoreList list = new TopScoreList(3);

        // first value should be added: list = (1)
        list.plus(1, 20);
        Assert.assertEquals(20, list.getMax());
        Assert.assertEquals(20, list.getMin());
        Assert.assertEquals(1, list.getSize());

        // new min value should be added: list = (20, -10)
        list.plus(2, -10);
        Assert.assertEquals(20, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(2, list.getSize());

        // value should be added to user 1: list = (80, -10)
        list.plus(1, 60);
        Assert.assertEquals(80, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(2, list.getSize());

        // value should be inserted within list: list = (80, 1, -10)
        list.plus(3, 1);
        Assert.assertEquals(80, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(3, list.getSize());

        // new min value shouldn't be added: list = (80, 1, -10)
        list.plus(4, -100);
        Assert.assertEquals(80, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(3, list.getSize());

        // new max value should be added: list = (100, 80, 1)
        list.plus(4, 100);
        Assert.assertEquals(100, list.getMax());
        Assert.assertEquals(1, list.getMin());
        Assert.assertEquals(3, list.getSize());

        // update score for exist user 3: list = (201, 100, 80)
        list.plus(3, 200);
        Assert.assertEquals(201, list.getMax());
        Assert.assertEquals(80, list.getMin());
        Assert.assertEquals(3, list.getSize());
    }
}
