package com.mineaurion.api.library.model.query;

public class Schedule {

    /**
     * Contains the timestamp of nextreboot
     */
    private Long nextReboot;

    public Schedule(Long nextReboot ) {
        this.nextReboot = nextReboot;
    }

    public Schedule(){};

    public Long getnextReboot() {
        return nextReboot;
    }

    public Schedule setnextReboot(Long nextReboot) {
        this.nextReboot = nextReboot;
        return this;
    }
}
