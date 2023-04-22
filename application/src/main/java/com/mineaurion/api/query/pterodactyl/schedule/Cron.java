package com.mineaurion.api.query.pterodactyl.schedule;

public class Cron{
    private String day_of_week;
    private String day_of_month;
    private String hour;
    private String minute;

    public String getDay_of_week() {
        return day_of_week;
    }

    public Cron setDay_of_week(String day_of_week) {
        this.day_of_week = day_of_week;
        return this;
    }

    public String getDay_of_month() {
        return day_of_month;
    }

    public Cron setDay_of_month(String day_of_month) {
        this.day_of_month = day_of_month;
        return this;

    }

    public String getHour() {
        return hour;
    }

    public Cron setHour(String hour) {
        this.hour = hour;
        return this;

    }

    public String getMinute() {
        return minute;
    }

    public Cron setMinute(String minute) {
        this.minute = minute;
        return this;
    }
}