package com.mineaurion.api.server;

import com.mineaurion.api.library.model.prometheus.PrometheusSD;
import com.mineaurion.api.server.model.Server;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/servers")
@SecurityRequirement(name = "bearerAuth")
public class ServerController {

    private final ServerService service;

    public ServerController(ServerService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    @Operation(
            parameters = {
                    @Parameter(name = "sortField", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "id"),
                    @Parameter(name = "sortOrder", in = ParameterIn.QUERY, schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"})),
            }
    )
    public ResponseEntity<List<Server>> findAll(
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

    @GetMapping("/{id}")
    public ResponseEntity<Server> find(@PathVariable("id") Long id) {
        return ResponseEntity.of(service.find(id));
    }

    @GetMapping("/externalid/{id}/")
    public ResponseEntity<Server> findByExternalId(@PathVariable("id") String id) {
        return ResponseEntity.of(service.findByExternalId(id));
    }

    @GetMapping("/prometheus-sd")
    public ResponseEntity<List<PrometheusSD>> prometheusServiceDiscovery(){
        return ResponseEntity.of(service.getPrometheusSD());
    }

    @PostMapping
    public ResponseEntity<Server> create(@Valid @RequestBody Server server) {
        Server created = service.create(server);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Server> update(@PathVariable("id") Long id, @Valid @RequestBody Server updatedServer) {
        Optional<Server> updated = service.update(id, updatedServer);
        return updated
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> {
                    Server created = service.create(updatedServer);
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.getId())
                            .toUri();
                    return ResponseEntity.created(location).body(created);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Server> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
