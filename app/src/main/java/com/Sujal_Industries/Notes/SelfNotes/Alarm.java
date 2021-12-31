package com.Sujal_Industries.Notes.SelfNotes;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Alarm extends SugarRecord {
    private String title;
    private String description;
    private String time;
    private int hour;
    private int minutes;
    private int unino;//Unique Number.

    @Ignore
    private boolean expanded;

    public Alarm() {
    }

    Alarm(String title, String description, String time, int hour, int minutes, int unino) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.hour = hour;
        this.minutes = minutes;
        this.unino = unino;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    boolean isExpanded() {
        return expanded;
    }

    void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    int getUnino() {
        return unino;
    }

    public void setUnino(int unino) {
        this.unino = unino;
    }
}
