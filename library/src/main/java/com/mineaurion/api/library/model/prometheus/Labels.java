package com.mineaurion.api.library.model.prometheus;

public class Labels {
    private String job;
    private String version;
    private String server;

    public Labels(String job, String version, String server) {
        this.job = job;
        this.version = version;
        this.server = server;
    }

    public Labels(String version, String server){
        this("minecraft", version, server);
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
