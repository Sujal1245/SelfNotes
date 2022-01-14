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

    public Alarm(String title, String description, String time, int hour, int minutes, int unino) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.hour = hour;
        this.minutes = minutes;
        this.unino = unino;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUnino() {
        return unino;
    }

    public void setUnino(int unino) {
        this.unino = unino;
    }
}
