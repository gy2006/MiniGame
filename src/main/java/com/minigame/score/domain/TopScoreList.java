package com.minigame.score.domain;

import java.util.*;

public class TopScoreList {

    private static class UserScore {

        private final Integer userId;

        private Integer score;

        public UserScore(Integer userId, Integer score) {
            this.userId = userId;
            this.score = score;
        }
    }

    private final int capacity;

    private final Map<Integer, UserScore> userMap;

    private final LinkedList<UserScore> list;

    private int max = Integer.MIN_VALUE;

    private int min = Integer.MIN_VALUE;

    public TopScoreList(int capacity) {
        this.capacity = capacity;
        this.list = new LinkedList<>();
        this.userMap = new HashMap<>(capacity + 1);
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getSize() {
        return list.size();
    }

    public void plus(int userId, int score) {
        UserScore us = userMap.get(userId);
        if (us != null) {
            us.score += score;

            list.sort(this::compare);
            max = list.getFirst().score;
            min = list.getLast().score;
            return;
        }

        us = new UserScore(userId, score);
        userMap.put(userId, us);
        list.add(us);
        list.sort(this::compare);

        if (userMap.size() > capacity) {
            UserScore min = list.removeLast();
            userMap.remove(min.userId);
        }

        max = list.getFirst().score;
        min = list.getLast().score;
    }

    private int compare(UserScore o1, UserScore o2) {
        return o2.score.compareTo(o1.score);
    }
}
