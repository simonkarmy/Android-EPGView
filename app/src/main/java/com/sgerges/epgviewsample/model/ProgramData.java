package com.sgerges.epgviewsample.model;

/**
 * Created by Simon Gerges on 5/29/17.
 * <p>
 */

public class ProgramData {

    private String title;
    private long startTime;
    private long endTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
