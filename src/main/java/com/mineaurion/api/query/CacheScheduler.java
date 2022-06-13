package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQueryException;
import com.mineaurion.api.query.lib.QueryResponse;
import com.mineaurion.api.server.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "scheduler", name = "cache", havingValue = "true")
public class CacheScheduler {
    Logger logger = LoggerFactory.getLogger(CacheScheduler.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private ServerService serverService;

    @Autowired
    private MinecraftQueryService minecraftQueryService;

    @Scheduled(fixedRate = 120000 )
    public void refreshCacheQueryServer(){
        logger.info("Refreshing query server cache");
        this.serverService.findAll().forEach(server -> {
            Cache cache = this.cacheManager.getCache("queryResponse");
            if(cache != null){
                this.taskExecutor.submit( () -> refreshCache(server.getAdministration().getQuery().getIp(), server.getAdministration().getQuery().getPort(), cache));
            }
        });
    }

    private void refreshCache(String address, Integer port, Cache cache){
        String cacheKey = address + "-" + port;
        try {
            QueryResponse queryResponse = this.minecraftQueryService.getQueryResponse(address, port);
            cache.putIfAbsent(cacheKey, queryResponse);
        } catch (MCQueryException e) {
            logger.info("The server %s:%s could updated in cache. Caused by : %s".formatted(address, port, e.getMessage()));
        }
    }
}
