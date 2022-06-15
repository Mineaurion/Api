package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQuery;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MinecraftQueryService {

    @Cacheable(cacheNames = {"queryResponse"}, key = "#address + '-' + #port")
    public MCQuery getQueryResponse(String address, Integer port) throws IOException {
        return new MCQuery(address, port);
    }
}
