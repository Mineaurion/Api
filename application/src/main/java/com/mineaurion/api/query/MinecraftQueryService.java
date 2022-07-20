package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MinecraftQueryService {

    Logger logger = LoggerFactory.getLogger(MinecraftQueryService.class);

    private final String errorLog = "The server %s with address %s:%s could not be contacted. Caused by : %s";

    @Cacheable(cacheNames = {"queryResponse"}, key = "#name + '-' + #address + '-' + #port")
    public MCQuery getQueryResponse(String name, String address, Integer port) {
        MCQuery mcQuery = new MCQuery(address, port);
        try {
            mcQuery.sendQueryRequest();
        } catch (IOException e){
            logger.error(errorLog.formatted(name, address, port, e.getMessage()));
        }
        return mcQuery;
    }
}
