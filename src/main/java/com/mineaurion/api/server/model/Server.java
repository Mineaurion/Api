package com.mineaurion.api.server.model;


import com.mineaurion.api.server.model.embeddable.Query;
import com.mineaurion.api.server.model.embeddable.Version;
import com.mineaurion.api.server.model.embeddable.*;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
// TODO: ajouter des options pour la supervision prometheus cf: https://prometheus.io/docs/prometheus/latest/configuration/configuration/#http_sd_config
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z\\d ]+$", message = "ne correspond pas à une chaine de caractère")
    @Schema(example = "infinity")
    private String name;

    @NotNull
    @Embedded
    @Valid
    private Version version;

    @NotNull
    @Pattern(regexp = "^(overworld|skyblock)$", message = "Le type doit être soit overworld soit skyblock")
    private String type;

    @NotNull
    private boolean beta;

    @NotNull
    @Embedded
    @Valid
    private WhiteList whiteList;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z .-]+$", message = "ne correspond pas à un nom de domaine")
    private String dns;

    @NotNull
    @Embedded
    @Valid
    private Query query;

    @NotNull
    @Embedded
    @Valid
    private Schedule schedule;

    @NotNull
    // @IsUUID(message = "ne correspond pas un uuid, prendre l'uuid au complet du serveur pterodactyl")
    private String externalId; // UUId server pterodactyl

    @NotNull
    @Embedded
    @Valid
    private Options options;

    public Server(Long id, String name, Version version, String type, boolean beta, WhiteList whiteList, String dns, Query query, Schedule schedule, String externalId, Options options) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.type = type;
        this.beta = beta;
        this.whiteList = whiteList;
        this.dns = dns;
        this.query = query;
        this.schedule = schedule;
        this.externalId = externalId;
        this.options = options;
    }

    public Server() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBeta() {
        return beta;
    }

    public void setBeta(boolean beta) {
        this.beta = beta;
    }

    public WhiteList getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(WhiteList whiteList) {
        this.whiteList = whiteList;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Server updateWith(Server server) {
        return new Server(
                this.id,
                server.name,
                server.version,
                server.type,
                server.beta,
                server.whiteList,
                server.dns,
                server.query,
                server.schedule,
                server.externalId,
                server.options
        );
    }
}