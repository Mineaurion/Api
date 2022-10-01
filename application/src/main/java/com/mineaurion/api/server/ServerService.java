package com.mineaurion.api.server;

import com.mineaurion.api.server.model.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServerService {

    @Autowired
    private ServerRepository repository;

    public List<Server> findAll(Sort.Direction sortDirection, String sortField, boolean hidden) {
        Server server = new Server();
        server.setHidden(hidden);
        return repository.findAll(Example.of(server), Sort.by(sortDirection, sortField));
    }

    public List<Server> findAll(Sort.Direction sortDirection, String sortField) {
        return repository.findAll(Sort.by(sortDirection, sortField));
    }

    public List<Server> findAll() {
        return this.findAll(Sort.Direction.ASC, "id");
    }

    public Optional<Server> find(Long id) {
        return repository.findById(id);
    }

    public Optional<Server> findByExternalId(String id) {
        return repository.findByAdministrationExternalId(id);
    }

    public Optional<Server> findByDns(String dns){
        return repository.findByDns(dns);
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
