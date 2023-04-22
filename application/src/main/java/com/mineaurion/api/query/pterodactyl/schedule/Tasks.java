package com.mineaurion.api.query.pterodactyl.schedule;

import java.util.List;

public class Tasks {
    private String object;

    private List<Task> data;

    public List<Task> getData() {
        return data;
    }

    public Tasks setAttributes(List<Task> data){
        this.data = data;
        return this;
    }
}
