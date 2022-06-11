package com.mineaurion.api.query.model;

import java.util.ArrayList;
import java.util.List;

public class OldQueryServer {

    private String dns;
    private String name;
    private String statut;
    private Integer players;
    private Integer maxplayers;
    private List<String> joueurs = new ArrayList<>();

    public OldQueryServer(String dns, String name) {
        this(dns, name, "Off", 0, 0);
    }

    public OldQueryServer(String dns, String name, Integer players, Integer maxplayers, List<String> joueurs) {
        this(dns, name, "On", players, maxplayers, joueurs);
    }
    public OldQueryServer(String dns, String name, String statut, Integer players, Integer maxplayers, List<String> joueurs) {
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

    public List<String> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(List<String> joueurs) {
        this.joueurs = joueurs;
    }
}
