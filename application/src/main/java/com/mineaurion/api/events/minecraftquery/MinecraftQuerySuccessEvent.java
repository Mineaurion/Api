package com.mineaurion.api.events.minecraftquery;

public class MinecraftQuerySuccessEvent extends MinecraftQueryEvent {

    public MinecraftQuerySuccessEvent(Object source, String name, String address, Integer port){
        super(source, name, address, port);
    }
}
