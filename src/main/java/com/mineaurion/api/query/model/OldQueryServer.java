package com.mineaurion.api.query.model;

/**
 * @deprecated use {@link QueryServer} instead.
 * The class will be deleted 2 month after the first release of this api.
 */
@Deprecated
public class OldQueryServer {

    private String dns;
    private String name;
    private String statut;
    private Integer players;
    private Integer maxplayers;
    private String[] joueurs = new String[0];

    public OldQueryServer(String dns, String name) {
        this(dns, name, "Off", 0, 0);
    }

    public OldQueryServer(String dns, String name, Integer players, Integer maxplayers, String[] joueurs) {
        this(dns, name, "On", players, maxplayers, joueurs);
    }

    public OldQueryServer(String dns, String name, String statut, Integer players, Integer maxplayers, String[] joueurs) {
        this.dns = dns;
        this.name = name;
        this.statut = statut;
        this.players = players;
        this.maxplayers = maxplayers;
        this.joueurs = joueurs;
    }

    public OldQueryServer(String dns, String name, String statut, Integer players, Integer maxplayers) {
        this.dns = dns;
        this.name = name;
        this.statut = statut;
        this.players = players;
        this.maxplayers = maxplayers;
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
