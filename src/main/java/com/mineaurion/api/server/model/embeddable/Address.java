package com.mineaurion.api.server.model.embeddable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Embeddable
public class Address {

    @NotNull
    @Pattern(regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", message = "ne correspond pas à une addresse ip")
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
