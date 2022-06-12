package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.query.lib.MCQueryException;
import com.mineaurion.api.query.lib.QueryResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MinecraftQueryService {

    @Cacheable(cacheNames = {"queryResponse"}, key = "#address + '-' + #port")
    public QueryResponse getQueryResponse(String address, Integer port) throws MCQueryException {
        return new MCQuery(address, port).fullStat();
    }
}
