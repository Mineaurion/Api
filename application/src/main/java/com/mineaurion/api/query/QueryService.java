package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.query.model.OldQueryServer;
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
        this.service.findAll(sortDirection, sortField).forEach(server -> {
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

    /**
     * @deprecated use {@link #getQueryServer(com.mineaurion.api.server.model.Server)} instead.
     * The method will be deleted 2 month after the first release of this api.
     */
    @Deprecated
    private OldQueryServer getOldQueryServer(com.mineaurion.api.server.model.Server server){
        String address = server.getAdministration().getQuery().getIp();
        Integer port = server.getAdministration().getQuery().getPort();
        MCQuery queryResponse = this.minecraftQueryService.getQueryResponse(server.getName(), address, port);
        return new OldQueryServer(server, queryResponse);
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

    /**
     * @deprecated use {@link #findOneByDns(String)} ()} instead.
     * The method will be deleted 2 month after the first release of this api.
     */
    @Deprecated
    public Optional<OldQueryServer> findOneOldQueryByDns(String dns){
        Optional<com.mineaurion.api.server.model.Server> server = this.service.findByDns(dns);
        return server.map(this::getOldQueryServer);
    }


}
