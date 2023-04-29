package com.mineaurion.api.events;

import com.mineaurion.api.events.minecraftquery.MinecraftQueryErrorEvent;
import com.mineaurion.api.events.minecraftquery.MinecraftQuerySuccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MinecraftQueryListener {

    @Autowired
    private Environment env;

    private final static Integer maxErrorCount = 5;

    private final static String errorLog = "The server %s with address %s:%s could not be contacted. Caused by : %s";

    Logger logger = LoggerFactory.getLogger(MinecraftQueryListener.class);


    public final Map<String, Integer> errorServer = new HashMap<>();

    @EventListener
    public void handleMinecraftServerErrorEvent(MinecraftQueryErrorEvent event){
        logger.info("Received spring custom event - " + event.getName());
        logger.error(errorLog.formatted(event.getName(), event.getAddress(), event.getPort(), event.getErrorMessage()));
        String key = event.getMapKey();
        errorServer.putIfAbsent(key, 0);
        if(errorServer.containsKey(key)){
            Integer errorCount = errorServer.get(key);
            if(errorCount >= maxErrorCount){
                // TOOD: need to re-work
                String content = env.getRequiredProperty("discord.webhook.message").formatted(event.getName(), maxErrorCount);
                logger.error(content);
            }
            errorServer.put(key, errorServer.get(key) + 1);
        }
    }

    @EventListener
    public void handleMinecraftServerSuccessEvent(MinecraftQuerySuccessEvent event){
        logger.info("Received spring custom success event - " + event.getName());

        String key = event.getMapKey();
        if(errorServer.containsKey(key)){
            logger.error("The server %s has come back online ".formatted(event.getName()));
            errorServer.remove(key);
        }

    }
}
