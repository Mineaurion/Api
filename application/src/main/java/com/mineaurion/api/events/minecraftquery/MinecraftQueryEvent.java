package com.mineaurion.api.events.minecraftquery;

import org.springframework.context.ApplicationEvent;

public class MinecraftQueryEvent extends ApplicationEvent {
    private final String name;
    private final String address;
    private final Integer port;

    public MinecraftQueryEvent(Object source, String name, String address, Integer port){
        super(source);
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public Integer getPort(){
        return port;
    }

    public String getMapKey(){
        return getName() + "-" + getAddress() + "-" + getPort();
    }
}
