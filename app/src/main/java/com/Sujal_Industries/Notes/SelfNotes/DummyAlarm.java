package com.Sujal_Industries.Notes.SelfNotes;

public class DummyAlarm {
    private String title;
    private String description;
    private String time;
    private int hour;
    private int minutes;
    private int unino;//Unique Number.

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
