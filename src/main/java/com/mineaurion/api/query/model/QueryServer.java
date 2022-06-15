package com.mineaurion.api.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.server.model.Server;
import com.mineaurion.api.server.model.embeddable.Administration;

public class QueryServer extends Server {

    @JsonIgnore
    private String externalId;

    @JsonIgnore
    private Administration administration;

    private boolean status = false;
    private Integer onlinePlayers = 0;
    private Integer maxPlayers = 0;
    private String[] players = new String[0];

    public QueryServer(Server server) {
        super(
                server.getId(),
                server.getName(),
                server.getVersion(),
                server.getType(),
                server.getAccess(),
                server.getDns(),
                server.getAdministration(),
                server.getSchedule()
        );
    }

    public QueryServer(Server server, MCQuery queryResponse) {
        this(server);
        this.status = true;
        this.onlinePlayers = queryResponse.getOnlinePlayers();
        this.maxPlayers = queryResponse.getMaxPlayers();
        this.players = queryResponse.getPlayerList();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(Integer onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }
}
