package com.mineaurion.api.query;

import com.mineaurion.api.Faker;
import com.mineaurion.api.query.lib.MCQueryResponse;

public class QueryFaker {

    private static final net.datafaker.Faker faker = Faker.faker;

    private static MCQueryResponse mcQuery(String address, Integer port, boolean status){
        MCQueryResponse mcQuery = new MCQueryResponse();
        mcQuery.setStatus(status);
        if(status) {
            mcQuery.setOnlinePlayers(faker.random().nextInt(0, 10));
            mcQuery.setMaxPlayers(faker.random().nextInt(0, 10));
            mcQuery.setPlayerList(faker.lorem().words().toArray(new String[0]));
        }
        return mcQuery;
    }

    public static MCQueryResponse mcQuery(boolean status){
        return mcQuery(faker.internet().ipV4Address(), faker.internet().port(), status);
    }

    public static MCQueryResponse mcQuery(com.mineaurion.api.server.model.Server server, boolean status){
        return mcQuery(server.getAdministration().getQuery().getIp(), server.getAdministration().getQuery().getPort(), status);
    }
}
