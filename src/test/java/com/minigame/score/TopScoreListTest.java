package com.minigame.score;

import com.minigame.score.domain.TopScoreList;
import com.minigame.util.StringHelper;
import org.junit.Assert;
import org.junit.Test;

public class TopScoreListTest {

    @Test
    public void shouldHandleListCorrectly() {
        TopScoreList list = new TopScoreList(3);
        Assert.assertEquals(StringHelper.EMPTY_STRING, list.toString());

        // first value should be added
        list.plus(1, 20);
        Assert.assertEquals(20, list.getMax());
        Assert.assertEquals(20, list.getMin());
        Assert.assertEquals(1, list.getSize());
        Assert.assertEquals("1=20", list.toString());

        // new min value should be added
        list.plus(2, -10);
        Assert.assertEquals(20, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(2, list.getSize());
        Assert.assertEquals("1=20,2=-10", list.toString());

        // value should be added to user 1
        list.plus(1, 60);
        Assert.assertEquals(80, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(2, list.getSize());
        Assert.assertEquals("1=80,2=-10", list.toString());

        // value should be inserted within list
        list.plus(3, 1);
        Assert.assertEquals(80, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(3, list.getSize());
        Assert.assertEquals("1=80,3=1,2=-10", list.toString());

        // new min value shouldn't be added
        list.plus(4, -100);
        Assert.assertEquals(80, list.getMax());
        Assert.assertEquals(-10, list.getMin());
        Assert.assertEquals(3, list.getSize());
        Assert.assertEquals("1=80,3=1,2=-10", list.toString());

        // new max value should be added
        list.plus(4, 100);
        Assert.assertEquals(100, list.getMax());
        Assert.assertEquals(1, list.getMin());
        Assert.assertEquals(3, list.getSize());
        Assert.assertEquals("4=100,1=80,3=1", list.toString());

        // update score for exist user 3: list = (201, 100, 80)
        list.plus(3, 200);
        Assert.assertEquals(201, list.getMax());
        Assert.assertEquals(80, list.getMin());
        Assert.assertEquals(3, list.getSize());
        Assert.assertEquals("3=201,4=100,1=80", list.toString());
    }
}
