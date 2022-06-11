package com.mineaurion.api.server;

import com.mineaurion.api.server.model.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServerService {

    @Autowired
    private ServerRepository repository;

    public List<Server> findAll() {
        return repository.findAll();
    }

    public Optional<Server> find(Long id) {
        return repository.findById(id);
    }

    public Optional<Server> findByExternalId(String id) {
        return repository.findByExternalId(id);
    }

    public Server create(Server server) {
        return repository.save(server);
    }

    public Optional<Server> update(Long id, Server newServer) {
        return repository.findById(id)
                .map(oldItem -> repository.save(oldItem.updateWith(newServer)));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
