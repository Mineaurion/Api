package com.mineaurion.api.server;

import com.mineaurion.api.server.model.Server;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/servers")
@SecurityRequirement(name = "bearer")
public class ServerController {

    private final ServerService service;

    public ServerController(ServerService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Server>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Server> find(@PathVariable("id") Long id){
        return ResponseEntity.of(service.find(id));
    }

    @PostMapping
    @Operation(summary = "Create a server", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Server> create(@Valid @RequestBody Server server){
        Server created = service.create(server);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a server", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Server> update(@PathVariable("id") Long id, @Valid @RequestBody Server updatedServer){
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
    @Operation(summary = "Delete a server", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Server> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach(error -> {
            map.put(
                    ((FieldError) error).getField(),
                    error.getDefaultMessage()
            );
        });
        return ResponseEntity.badRequest().body(map);
    }
}
