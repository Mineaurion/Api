package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.library.model.query.Server;
import com.mineaurion.api.server.ServerService;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableAsync
public class QueryService {

    private final ServerService service;
    private final MinecraftQueryService minecraftQueryService;

    public QueryService(ServerService service, MinecraftQueryService minecraftQueryService) {
        this.service = service;
        this.minecraftQueryService = minecraftQueryService;
    }

    private Server getQueryServer(com.mineaurion.api.server.model.Server server){
        String address = server.getAdministration().getQuery().getIp();
        Integer port = server.getAdministration().getQuery().getPort();
        MCQuery query = this.minecraftQueryService.getQueryResponse(server.getName(), address, port);
        return server
                .toQueryServer()
                .setStatus(query.getStatus())
                .setOnlinePlayers(query.getOnlinePlayers())
                .setMaxPlayers(query.getMaxPlayers())
                .setPlayers(query.getPlayerList());
    }

    public List<Server> findAll(Sort.Direction sortDirection, String sortField) {
        List<Server> list = new ArrayList<>();
        this.service.findAll(sortDirection, sortField, false).forEach(server -> {
            list.add(this.getQueryServer(server));
        });
        return list;
    }

    public List<Server> findAll() {
        return this.findAll(Sort.Direction.ASC, "id");
    }

    public Optional<Server> findOneByDns(String dns) {
        Optional<com.mineaurion.api.server.model.Server> server = this.service.findByDns(dns);
        return server.map(this::getQueryServer);
    }

    public Integer getPlayerCount() {
        AtomicInteger numberOfPlayerOnline = new AtomicInteger();
        findAll().forEach(queryServer -> {
            numberOfPlayerOnline.addAndGet(queryServer.getOnlinePlayers());
        });
        return numberOfPlayerOnline.get();
    }
}
