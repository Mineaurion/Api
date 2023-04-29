package com.mineaurion.api.query;

import com.mineaurion.api.events.minecraftquery.MinecraftQueryErrorEvent;
import com.mineaurion.api.events.minecraftquery.MinecraftQuerySuccessEvent;
import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.query.lib.MCQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MinecraftQueryService {

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final MCQuery mcQuery;

    public MinecraftQueryService(MCQuery mcQuery){
        this.mcQuery = mcQuery;
    }

    @Cacheable(cacheNames = {"queryResponse"}, key = "#name + '-' + #address + '-' + #port")
    public MCQueryResponse getQueryResponse(String name, String address, Integer port) {
        MCQueryResponse mcQueryResponse = new MCQueryResponse();
        try {
            mcQueryResponse = mcQuery.sendQueryRequest(address, port);
            applicationEventPublisher.publishEvent(new MinecraftQuerySuccessEvent(this, name, address, port));
        } catch (IOException e){
            applicationEventPublisher.publishEvent(new MinecraftQueryErrorEvent(this, name, address, port, e.getMessage()));
        }
        return mcQueryResponse;
    }
}
