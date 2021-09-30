package com.minigame.score;

import com.minigame.score.domain.MaxList;
import com.minigame.score.domain.UserScore;
import org.junit.Assert;
import org.junit.Test;

public class MaxListTest {

    @Test
    public void ShouldHandleMaxListCorrectly() {
        MaxList list = new MaxList(5);

        list.add(new UserScore(1, 20));
        Assert.assertEquals(20, list.getMax());
        Assert.assertEquals(20, list.getMin());

        list.add(new UserScore(1, 16));
        Assert.assertEquals(20, list.getMax());
        Assert.assertEquals(16, list.getMin());

        list.add(new UserScore(1, 30));
        Assert.assertEquals(30, list.getMax());
        Assert.assertEquals(16, list.getMin());

        list.add(new UserScore(1, 100));
        Assert.assertEquals(100, list.getMax());
        Assert.assertEquals(16, list.getMin());

        list.add(new UserScore(1, 1));
        Assert.assertEquals(100, list.getMax());
        Assert.assertEquals(1, list.getMin());

        list.add(new UserScore(1, 40));
        Assert.assertEquals(100, list.getMax());
        Assert.assertEquals(16, list.getMin());
        Assert.assertEquals(5, list.getSize());
    }
}
