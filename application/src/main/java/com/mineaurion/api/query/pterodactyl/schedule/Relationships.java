package com.mineaurion.api.query.pterodactyl.schedule;

public class Relationships{
    private Tasks tasks;

    public Tasks getTasks() {
        return tasks;
    }

    public Relationships setTasks(Tasks tasks) {
        this.tasks = tasks;
        return this;
    }
}
