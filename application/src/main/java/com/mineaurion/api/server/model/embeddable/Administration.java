package com.mineaurion.api.server.model.embeddable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Embeddable
public class Administration {
    @NotNull
    private boolean regen;

    @NotNull
    @Embedded
    @Valid
    private Address query;

    @NotNull
    private UUID externalId; // UUId server pterodactyl

    public Administration(boolean regen, Address query, UUID externalId) {
        this.regen = regen;
        this.query = query;
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

    public Address getQuery() {
        return query;
    }

    public void setQuery(Address query) {
        this.query = query;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }
}
