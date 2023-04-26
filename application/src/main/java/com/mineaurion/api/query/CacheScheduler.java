package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQuery;
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

    @Scheduled(fixedRate = 120 * 1000)
    public void refreshCacheQueryServer(){
        logger.info("Refreshing query server cache");
        Cache cache = this.cacheManager.getCache("queryResponse");
        if(cache != null){
            cache.clear();
            this.serverService.findAll().forEach(server -> {
                this.taskExecutor.submit( () -> refreshCache(server.getName(), server.getAdministration().getQuery().getIp(), server.getAdministration().getQuery().getPort(), cache));
            });

        }
    }

    @Scheduled(fixedRate = 120 * 1000)
    public void deleteNextRebootScheduleCache(){
        logger.info("Delete nextRebootSchedule cache");
        Cache cache = this.cacheManager.getCache("nextRebootSchedule");
        if(cache != null){
            cache.clear();
        }
    }

    private void refreshCache(String name, String address, Integer port, Cache cache){
        String cacheKey = "%s-%s-%s".formatted(name, address, port);
        MCQuery queryResponse = this.minecraftQueryService.getQueryResponse(name, address, port);
        cache.putIfAbsent(cacheKey, queryResponse);
    }
}
