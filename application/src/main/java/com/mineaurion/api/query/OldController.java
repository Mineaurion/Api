package com.mineaurion.api.query;

import com.mineaurion.api.query.model.OldQueryServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @deprecated See the new controller {@link QueryController}.
 * The class will be deleted 2 month after the first release of this api.
 */
@RestController
@RequestMapping("/v1")
@Deprecated
public class OldController {
    private final QueryService service;

    public OldController(QueryService service) {
        this.service = service;
    }

    @GetMapping("/serveurs")
    public ResponseEntity<List<OldQueryServer>> getAllQuery() {
        return ResponseEntity.ok().body(service.findAllOldQuery());
    }

    @GetMapping("/serveurs/{dns}")
    public ResponseEntity<OldQueryServer> getOneQueryByDns(@PathVariable("dns") String dns){
        return ResponseEntity.of(service.findOneOldQueryByDns(dns));
    }

    @GetMapping("/website/home")
    public ResponseEntity<Map<String, Integer>> websiteHome() {
        return ResponseEntity.ok().body(Collections.singletonMap("joueurs", service.getPlayerCount()));
    }
}
