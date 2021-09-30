package com.minigame.score.domain;

import java.util.LinkedList;

public class MaxList {

    private final int capacity;

    private final LinkedList<UserScore> list;

    private int max = Integer.MIN_VALUE;

    private int min = Integer.MIN_VALUE;

    public MaxList(int capacity) {
        this.capacity = capacity;
        this.list = new LinkedList<>();
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

    public void add(UserScore in) {
        if (list.size() < capacity) {
            addWhenNotFull(in);
            return;
        }

        addWhenFull(in);
    }

    private void addWhenFull(UserScore in) {
        if (in.getScore() == min || in.getScore() == max) {
            return;
        }

        if (in.getScore() < min) {
            return;
        }

        if (in.getScore() > max) {
            list.addFirst(in);
            max = in.getScore();
            resetMin();
            return;
        }

        int i = 0;
        for (UserScore item : list) {
            if (item.getScore() < in.getScore()) {
                list.add(i, in);
                resetMin();
                return;
            }
            i++;
        }
    }

    private void resetMin() {
        list.pollLast();
        min = list.getLast().getScore();
    }

    private void addWhenNotFull(UserScore in) {
        if (in.getScore() >= max) {
            list.addFirst(in);
        }

        if (in.getScore() <= min) {
            list.addLast(in);
        }

        max = list.peekFirst().getScore();
        min = list.peekLast().getScore();
    }
}
