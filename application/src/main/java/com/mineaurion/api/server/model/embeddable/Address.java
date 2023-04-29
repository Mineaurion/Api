package com.mineaurion.api.server.model.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class Address {

    @NotEmpty
    @Pattern(regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", message = "ne correspond pas à une adresse ip")
    private String ip;

    @NotNull
    private int port;

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Address() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
