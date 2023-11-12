package com.mineaurion.api.server;

import com.mineaurion.api.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerRepository extends JpaRepository<Server, Long> {

    Optional<Server> findByAdministrationExternalId(UUID externalId);

    Optional<List<Server>> findByAdministrationPrometheusIsNotNull();

    Optional<Server> findByDns(String dns);
}
