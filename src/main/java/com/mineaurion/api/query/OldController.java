package com.mineaurion.api.query;

import com.mineaurion.api.query.model.OldQueryServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @deprecated See the new controller {@link QueryController}.
 * The class will be deleted 2 month after the first release of this api.
 */
@RestController
@RequestMapping("/v1/serveurs")
@Deprecated
public class OldController {
    private final QueryService service;

    public OldController(QueryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OldQueryServer>> getAllQuery() {
        return ResponseEntity.ok().body(service.findAllOldQuery());
    }
}
