package com.mineaurion.api.events.minecraftquery;

public class MinecraftQueryErrorEvent extends MinecraftQueryEvent {
    private final String errorMessage;

    public MinecraftQueryErrorEvent(Object source, String name, String address, Integer port, String errorMessage){
        super(source, name, address, port);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
