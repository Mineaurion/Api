package com.mineaurion.api.library.model.query;

public class Server {
    private String name;
    private Version version;
    private String type;
    private Access access;
    private String dns;
    private Schedule schedule;
    private boolean status = false;
    private Integer onlinePlayers = 0;
    private Integer maxPlayers = 0;
    private String[] players = new String[0];

    public Server(
            String name,
            Version version,
            String type,
            Access access,
            String dns,
            Schedule schedule,
            boolean status,
            Integer onlinePlayers,
            Integer maxPlayers,
            String[] players
    ) {
        this.name = name;
        this.version = version;
        this.type = type;
        this.access = access;
        this.dns = dns;
        this.schedule = schedule;
        this.status = status;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.players = players;
    }

    public Server(
            String name,
            Version version,
            String type,
            Access access,
            String dns,
            Schedule schedule
    ) {
        this.name = name;
        this.version = version;
        this.type = type;
        this.access = access;
        this.dns = dns;
        this.schedule = schedule;
    }

    public Server(){}

    public String getName() {
        return name;
    }

    public Server setName(String name) {
        this.name = name;
        return this;
    }

    public Version getVersion() {
        return version;
    }

    public Server setVersion(Version version) {
        this.version = version;
        return this;
    }

    public String getType() {
        return type;
    }

    public Server setType(String type) {
        this.type = type;
        return this;
    }

    public Access getAccess() {
        return access;
    }

    public Server setAccess(Access access) {
        this.access = access;
        return this;
    }

    public String getDns() {
        return dns;
    }

    public Server setDns(String dns) {
        this.dns = dns;
        return this;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Server setSchedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public boolean isStatus() {
        return status;
    }

    public Server setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public Integer getOnlinePlayers() {
        return onlinePlayers;
    }

    public Server setOnlinePlayers(Integer onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        return this;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public Server setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public String[] getPlayers() {
        return players;
    }

    public Server setPlayers(String[] players) {
        this.players = players;
        return this;
    }
}

