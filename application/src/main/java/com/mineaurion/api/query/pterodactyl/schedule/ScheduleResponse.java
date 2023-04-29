package com.mineaurion.api.query.pterodactyl.schedule;

import java.util.List;

public class ScheduleResponse {
    private String object;

    private List<Schedule> data;

    public String getObject() {
        return object;
    }

    public ScheduleResponse setObject(String object) {
        this.object = object;
        return this;
    }

    public  List<Schedule> getData() {
        return data;
    }

    public ScheduleResponse setAttributes( List<Schedule> data) {
        this.data = data;
        return this;
    }
}
