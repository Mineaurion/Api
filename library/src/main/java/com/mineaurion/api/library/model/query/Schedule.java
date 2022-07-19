package com.mineaurion.api.library.model.query;

import java.util.List;

public class Schedule {
    private List<String> reboot;

    public Schedule(List<String> reboot) {
        this.reboot = reboot;
    }

    public List<String> getReboot() {
        return reboot;
    }

    public Schedule setReboot(List<String> reboot) {
        this.reboot = reboot;
        return this;
    }
}
