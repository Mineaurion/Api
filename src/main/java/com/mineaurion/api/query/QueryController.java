package com.mineaurion.api.query;

import com.mineaurion.api.query.model.QueryServer;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/query")
public class QueryController {
    private final QueryService service;

    public QueryController(QueryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get query response from all the server")
    public ResponseEntity<List<QueryServer>> getAllQuery() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/online-players")
    @Operation(summary = "Get number of player online")
    public ResponseEntity<Map<String, Integer>> getOnlinePlayers() {
        return ResponseEntity.ok().body(Collections.singletonMap("onlinePlayers", service.getPlayerCount()));
    }
}
