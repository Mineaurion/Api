package com.mineaurion.api.query;

import com.mineaurion.api.library.model.query.Schedule;
import com.mineaurion.api.library.model.query.Server;
import com.mineaurion.api.query.lib.MCQueryResponse;
import com.mineaurion.api.server.ServerService;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableAsync
public class QueryService {

    private final ServerService service;
    private final MinecraftQueryService minecraftQueryService;

    private final PterodactylQueryService pterodactylQueryService;

    public QueryService(ServerService service, MinecraftQueryService minecraftQueryService, PterodactylQueryService pterodactylQueryService) {
        this.service = service;
        this.minecraftQueryService = minecraftQueryService;
        this.pterodactylQueryService = pterodactylQueryService;
    }

    private Server getQueryServer(com.mineaurion.api.server.model.Server server){
        MCQueryResponse query = this.minecraftQueryService.getQueryResponse(
                server.getName(),
                server.getAdministration().getQuery().getIp(),
                server.getAdministration().getQuery().getPort())
                ;

        Server queryServer = server
                .toQueryServer()
                .setStatus(query.getStatus())
                .setOnlinePlayers(query.getOnlinePlayers())
                .setMaxPlayers(query.getMaxPlayers())
                .setPlayers(query.getPlayerList())
                ;
        Optional<Date> nextReboot = pterodactylQueryService.getNextRebootSchedule(server.getAdministration().getExternalId());
        nextReboot.ifPresent(date -> queryServer.setSchedule(new Schedule(date.getTime() / 1000)));
        return queryServer;
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
