package com.mineaurion.api.server.model.embeddable;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Embeddable
public class Administration {
    @NotNull
    private boolean regen;

    @NotNull
    private boolean backup;

    @NotNull
    @Embedded
    @Valid
    private Address query;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="ip", column=@Column(name = "prometheus_ip")),
            @AttributeOverride(name="port", column=@Column(name = "prometheus_port"))
    })
    @Valid
    private Address prometheus;

    @NotNull
    private UUID externalId; // UUId server pterodactyl

    public Administration(boolean regen, boolean backup, Address query, Address prometheus, UUID externalId) {
        this.regen = regen;
        this.backup = backup;
        this.query = query;
        this.prometheus = prometheus;
        this.externalId = externalId;
    }

    public Administration(){

    }

    public boolean isRegen() {
        return regen;
    }

    public void setRegen(boolean regen) {
        this.regen = regen;
    }

    public boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    public Address getQuery() {
        return query;
    }

    public void setQuery(Address query) {
        this.query = query;
    }

    public Address getPrometheus() {
        return prometheus;
    }

    public void setPrometheus(Address prometheus) {
        this.prometheus = prometheus;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }
}
