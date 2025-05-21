package com.example.Models.utilities;

public class Game {
    private long startTime;
    private long duration;

    public Game(long startTime, long duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
