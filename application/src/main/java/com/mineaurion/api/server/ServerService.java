package com.mineaurion.api.server;

import com.mineaurion.api.library.model.prometheus.Labels;
import com.mineaurion.api.library.model.prometheus.PrometheusSD;
import com.mineaurion.api.server.model.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    public Optional<List<Server>> findAllPrometheus() {
        return repository.findByAdministrationPrometheusIsNotNull();
    }

    public Optional<List<PrometheusSD>> getPrometheusSD(){
        Optional<List<Server>> servers = this.findAllPrometheus();
        List<PrometheusSD> prometheusSd = new ArrayList<>();
        servers.ifPresent(serverList -> serverList.forEach(server -> {
            prometheusSd.add(
                    new PrometheusSD(
                            server.getAdministration().getPrometheus().getIp() + ":" + server.getAdministration().getPrometheus().getPort(),
                            new Labels(server.getVersion().getMinecraft(), server.getName())
                    )
            );
        }));
        return Optional.of(prometheusSd);
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
