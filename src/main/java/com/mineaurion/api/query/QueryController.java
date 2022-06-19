package com.mineaurion.api.query;

import com.mineaurion.api.query.model.QueryServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    @Operation(
            summary = "Get query response from all the server",
            parameters = {
                    @Parameter(name = "sortField", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "id"),
                    @Parameter(name = "sortOrder", in = ParameterIn.QUERY, schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}))
            }
    )
    public ResponseEntity<List<QueryServer>> getAllQuery(
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOder
    ) {
        try {
            Sort.Direction sortDirection = Sort.Direction.valueOf(sortOder);
            return ResponseEntity.ok().body(service.findAll(sortDirection, sortField));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{dns}")
    @Operation(summary = "Get query response for a server by dns")
    public ResponseEntity<QueryServer> getOneQuery(@PathVariable("dns") String dns){
        return ResponseEntity.of(service.findOneByDns(dns));
    }

    @GetMapping("/online-players")
    @Operation(summary = "Get number of player online")
    public ResponseEntity<Map<String, Integer>> getOnlinePlayers() {
        return ResponseEntity.ok().body(Collections.singletonMap("onlinePlayers", service.getPlayerCount()));
    }
}
