package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQueryException;
import com.mineaurion.api.query.lib.QueryResponse;
import com.mineaurion.api.query.model.OldQueryServer;
import com.mineaurion.api.query.model.QueryServer;
import com.mineaurion.api.server.ServerService;
import com.mineaurion.api.server.model.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableAsync
public class QueryService {

    Logger logger = LoggerFactory.getLogger(QueryService.class);
    private final String errorLog = "The server %s:%s could not be contacted. Caused by : %s";

    private final ServerService service;
    private final MinecraftQueryService minecraftQueryService;

    public QueryService(ServerService service, MinecraftQueryService minecraftQueryService) {
        this.service = service;
        this.minecraftQueryService = minecraftQueryService;
    }

    private QueryServer getQueryServer(Server server){
        try {
            QueryResponse query = this.minecraftQueryService.getQueryResponse(server.getQuery().getIp(), server.getQuery().getPort());
            return new QueryServer(server, query);
        } catch (MCQueryException e) {
            logger.error(errorLog.formatted(server.getQuery().getIp(), server.getQuery().getPort(), e.getMessage()));
            return new QueryServer(server);
        }
    }

    public List<QueryServer> findAll() {
        List<QueryServer> list = new ArrayList<>();
        this.service.findAll().forEach(server -> {
            list.add(this.getQueryServer(server));
        });
        return list;
    }

    public Integer getPlayerCount() {
        AtomicInteger numberOfPlayerOnline = new AtomicInteger();
        findAll().forEach(queryServer -> {
            numberOfPlayerOnline.addAndGet(queryServer.getOnlinePlayers());
        });
        return numberOfPlayerOnline.get();
    }

    /**
     * @deprecated use {@link #getQueryServer(Server)} instead.
     * The method will be deleted 2 month after the first release of this api.
     */
    @Deprecated
    private OldQueryServer getOldQueryServer(Server server){
        try{
            QueryResponse queryResponse = this.minecraftQueryService.getQueryResponse(server.getQuery().getIp(), server.getQuery().getPort());
            return new OldQueryServer(server.getDns(), server.getName(), queryResponse.getOnlinePlayers(), queryResponse.getMaxPlayers(), queryResponse.getPlayerList());
        } catch (MCQueryException e) {
            logger.error(errorLog.formatted(server.getQuery().getIp(), server.getQuery().getPort(), e.getMessage()));
            return new OldQueryServer(server.getDns(), server.getName());
        }
    }

    /**
     * @deprecated use {@link #findAll()} instead.
     * The method will be deleted 2 month after the first release of this api.
     */
    @Deprecated
    public List<OldQueryServer> findAllOldQuery(){
        List<OldQueryServer> list = new ArrayList<>();
        this.service.findAll().forEach(server -> {
            list.add(this.getOldQueryServer(server));
        });
        return list;
    }


}
