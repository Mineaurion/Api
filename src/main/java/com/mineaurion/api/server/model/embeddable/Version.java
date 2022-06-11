package com.mineaurion.api.server.model.embeddable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Version {

    @NotNull
    private String minecraft;

    @NotNull
    private String modpack;

    public Version(String minecraft, String modpack) {
        this.minecraft = minecraft;
        this.modpack = modpack;
    }
    public Version(){}

    public String getMinecraft() {
        return minecraft;
    }

    public void setMinecraft(String minecraft) {
        this.minecraft = minecraft;
    }

    public String getModpack() {
        return modpack;
    }

    public void setModpack(String modpack) {
        this.modpack = modpack;
    }
}
