package com.mineaurion.api.query;

import com.mineaurion.api.query.lib.MCQueryException;
import com.mineaurion.api.query.lib.QueryResponse;
import com.mineaurion.api.query.model.OldQueryServer;
import com.mineaurion.api.server.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/serveurs")
public class OldController {
    // TODO: class will be deleted in 2 month
    private final ServerService serverService;
    private final QueryService queryService;

    public OldController(ServerService serverService, QueryService queryService){
        this.serverService = serverService;
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<List<OldQueryServer>> getAllQuery(){
        List<OldQueryServer> returnList = new ArrayList<>();
        this.serverService.findAll().forEach(server -> {
            try {
                QueryResponse queryResponse = this.queryService.getQueryResponse(server.getQuery().getIp(), server.getQuery().getPort());
                returnList.add(
                        new OldQueryServer(server.getDns(), server.getName(), queryResponse.getOnlinePlayers(), queryResponse.getMaxPlayers(), queryResponse.getPlayerList())
                );
            } catch (MCQueryException e){
                returnList.add(
                        new OldQueryServer(server.getDns(), server.getName())
                );
            }
        });
        return ResponseEntity.ok().body(returnList);
    }
}
