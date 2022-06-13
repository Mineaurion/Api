package com.mineaurion.api.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mineaurion.api.query.lib.QueryResponse;
import com.mineaurion.api.server.model.Server;
import com.mineaurion.api.server.model.embeddable.Administration;

import java.util.ArrayList;
import java.util.List;

public class QueryServer extends Server {

    @JsonIgnore
    private String externalId;

    @JsonIgnore
    private Administration administration;

    private boolean status = false;
    private Integer onlinePlayers = 0;
    private Integer maxPlayers = 0;
    private List<String> players = new ArrayList<>();

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

    public QueryServer(Server server, QueryResponse queryResponse) {
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

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
