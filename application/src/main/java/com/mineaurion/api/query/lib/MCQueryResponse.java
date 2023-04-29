package com.mineaurion.api.query.lib;

public class MCQueryResponse {

    private String[] playerList = new String[0];

    private boolean status = false;

    private Integer onlinePlayers = 0;

    private Integer maxPlayers = 0;

    public String[] getPlayerList() {
        return playerList;
    }

    public MCQueryResponse setPlayerList(String[] playerList) {
        this.playerList = playerList;
        return this;
    }

    public boolean getStatus() {
        return status;
    }

    public MCQueryResponse setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public Integer getOnlinePlayers() {
        return onlinePlayers;
    }

    public MCQueryResponse setOnlinePlayers(Integer onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        return this;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public MCQueryResponse setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }
}
