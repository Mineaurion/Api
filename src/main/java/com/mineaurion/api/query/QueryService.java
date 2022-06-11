package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.query.lib.MCQueryException;
import com.mineaurion.api.query.lib.QueryResponse;
import com.mineaurion.api.query.model.QueryServer;
import com.mineaurion.api.server.ServerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {
    private final ServerService service;

    public QueryService(ServerService service) {
        this.service = service;
    }

    public QueryResponse getQueryResponse(String address, Integer port) throws MCQueryException {
        return new MCQuery(address, port).fullStat();
    }

    public List<QueryServer> findAll() {
        List<QueryServer> list = new ArrayList<>();
        this.service.findAll().forEach(server -> {
            QueryResponse query = null;
            try {
                query = getQueryResponse(server.getQuery().getIp(), server.getQuery().getPort());
                list.add(
                        new QueryServer(server, query)
                );
            } catch (MCQueryException e) {
                list.add(
                        new QueryServer(server)
                );
            }
        });
        return list;
    }
}
