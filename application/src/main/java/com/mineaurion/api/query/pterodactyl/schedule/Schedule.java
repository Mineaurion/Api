package com.mineaurion.api.query.pterodactyl.schedule;

public class Schedule {
    private String object;

    private Attributes attributes;

    public String getObject() {
        return object;
    }

    public Schedule setObject(String object) {
        this.object = object;
        return this;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Schedule setAttributes(Attributes attributes) {
        this.attributes = attributes;
        return this;
    }
}
