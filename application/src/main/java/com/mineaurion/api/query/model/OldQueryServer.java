package com.mineaurion.api.query.model;

import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.server.model.Server;


/**
 * @deprecated use {@link com.mineaurion.api.library.model.query.Server} instead.
 * The class will be deleted 2 month after the first release of this api.
 */
@Deprecated
public class OldQueryServer {

    private String dns;
    private String name;
    private String statut;
    private Integer players;
    private Integer maxplayers;
    private String[] joueurs;

    public OldQueryServer(Server server, MCQuery query){
        this.dns = server.getDns();
        this.name = server.getName();
        this.statut = query.getStatus() ? "On" : "Off";
        this.players = query.getOnlinePlayers();
        this.maxplayers = query.getMaxPlayers();
        this.joueurs = query.getPlayerList();
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public Integer getMaxplayers() {
        return maxplayers;
    }

    public void setMaxplayers(Integer maxplayers) {
        this.maxplayers = maxplayers;
    }

    public String[] getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(String[] joueurs) {
        this.joueurs = joueurs;
    }
}
