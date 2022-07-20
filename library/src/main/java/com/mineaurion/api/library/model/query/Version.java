package com.mineaurion.api.library.model.query;

public class Version {
    private String minecraft;

    private String modpack;

    public Version(String minecraft, String modpack) {
        this.minecraft = minecraft;
        this.modpack = modpack;
    }

    public String getMinecraft() {
        return minecraft;
    }

    public Version setMinecraft(String minecraft) {
        this.minecraft = minecraft;
        return this;
    }

    public String getModpack() {
        return modpack;
    }

    public Version setModpack(String modpack) {
        this.modpack = modpack;
        return this;
    }
}
