package com.Sujal_Industries.Notes.SelfNotes;

import androidx.annotation.Keep;

@Keep
public class DummyAlarm {
    public String title;
    public String description;
    public String time;
    public int hour;
    public int minutes;
    public int unino;//Unique Number.

    public DummyAlarm() {
    }

    public DummyAlarm(String title, String description, String time, int hour, int minutes, int unino) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public int getUnino() {
        return unino;
    }

    public void setUnino(int unino) {
        this.unino = unino;
    }
}
