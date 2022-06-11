package com.mineaurion.api.query.lib;

public class MCQueryException extends Exception{
    public MCQueryException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
