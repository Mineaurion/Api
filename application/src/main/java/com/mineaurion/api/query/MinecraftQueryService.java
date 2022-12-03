package com.mineaurion.api.query;

import com.mineaurion.api.discord.Webhook;
import com.mineaurion.api.query.lib.MCQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MinecraftQueryService {

    @Autowired
    private Environment env;

    Logger logger = LoggerFactory.getLogger(MinecraftQueryService.class);

    private final String errorLog = "The server %s with address %s:%s could not be contacted. Caused by : %s";

    private final Map<String, Integer> errorServer = new HashMap<>();

    @Cacheable(cacheNames = {"queryResponse"}, key = "#name + '-' + #address + '-' + #port")
    public MCQuery getQueryResponse(String name, String address, Integer port) {
        MCQuery mcQuery = new MCQuery(address, port);
        try {
            mcQuery.sendQueryRequest();
            // TODO: temp workaround need a better one
            this.resetToZero(address, name);
        } catch (IOException e){
            if(this.errorServer.containsKey(address) && this.errorServer.get(address) == 30){
                try {
                    String content = env.getProperty("discord.webhook.message").formatted(name);
                    new Webhook(env.getProperty("discord.webhook"))
                            .setContent(content)
                            .execute();
                } catch (IOException exception){
                    logger.error("Error when sending the discord webhook " + exception.getMessage());
                }
                this.resetToZero(address, name);
            }
            this.errorServer.put(address, this.errorServer.getOrDefault(address, 0) + 1);
            logger.error(errorLog.formatted(name, address, port, e.getMessage()));
        }
        return mcQuery;
    }

    private void resetToZero(String address, String name) {
        logger.info("Reset to zero the number of query request attempts for " + name);
        this.errorServer.put(address, 0);
    }
}
