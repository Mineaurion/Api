package com.mineaurion.api.server;

import com.mineaurion.api.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, Long> {
    Optional<Server> findByAdministrationExternalId(String externalId);

    Optional<Server> findByDns(String dns);
}
