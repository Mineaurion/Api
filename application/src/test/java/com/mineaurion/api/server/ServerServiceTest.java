package com.mineaurion.api.server;

import com.mineaurion.api.Faker;
import com.mineaurion.api.server.model.Server;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ServerServiceTest {

    @Mock
    private ServerRepository repository;

    @InjectMocks
    private ServerService service;

    @Test
    public void should_find_and_return_all_server_by_hidden_with_sort_direction_and_field(){
        // Arrange
        final var servers = Faker.listServer();
        Server server = new Server();
        boolean hidden = Faker.faker.random().nextBoolean();
        server.setHidden(true);
        Sort.Direction sortDirection = Faker.faker.options().option(Sort.Direction.ASC, Sort.Direction.DESC);
        String sortField = Faker.faker.options().option("id", "name");
        when(repository.findAll(Example.of(server), Sort.by(sortDirection, sortField))).thenReturn(servers);

        // Act
        final var actual = service.findAll(sortDirection, sortField, hidden);

        // Assert
        assertThat(actual.size()).isLessThanOrEqualTo(servers.size());
        verify(repository, times(1)).findAll(any(Example.class), eq(Sort.by(sortDirection, sortField)));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_find_and_return_all_server_with_sort_direction_and_field(){
        // Arrange
        final var servers = Faker.listServer();
        Sort.Direction sortDirection = Faker.faker.options().option(Sort.Direction.ASC, Sort.Direction.DESC);
        String sortField = Faker.faker.options().option("id", "name");
        when(repository.findAll(Sort.by(sortDirection, sortField))).thenReturn(servers);

        // Act
        final var actual = service.findAll(sortDirection, sortField);

        // Assert
        assertThat(actual.size()).isLessThanOrEqualTo(servers.size());
        verify(repository, times(1)).findAll(eq(Sort.by(sortDirection, sortField)));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_find_and_return_all_server_sorted_asc_id_by_default(){
        // Arrange
        final var servers = Faker.listServer();
        when(repository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(servers);

        // Act
        final var actual = service.findAll();

        // Assert
        assertThat(actual.size()).isEqualTo(servers.size());
        verify(repository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "id"));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_find_and_return_one_server(){
        // Arrange
        final var expectServer = Faker.server();
        when(repository.findById(anyLong())).thenReturn(Optional.of(expectServer));

        // Act
        final var actual = service.find(Faker.faker.random().nextLong());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectServer));
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_find_by_externalId_and_return_one_server(){
        // Arrange
        final var expectServer = Faker.server();
        when(repository.findByAdministrationExternalId(any(UUID.class))).thenReturn(Optional.of(expectServer));

        // Act
        final var actual = service.findByExternalId(UUID.randomUUID());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectServer));
        verify(repository, times(1)).findByAdministrationExternalId(any((UUID.class)));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_find_by_dns_and_return_one_server(){
        // Arrange
        final var expectServer = Faker.server();
        when(repository.findByDns(anyString())).thenReturn(Optional.of(expectServer));

        // Act
        final var actual = service.findByDns(Faker.faker.internet().domainName());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectServer));
        verify(repository, times(1)).findByDns(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_find_all_prometheus(){
        // Arrange
        final var servers = Faker.listServer();
        when(repository.findByAdministrationPrometheusIsNotNull()).thenReturn(Optional.of(servers));

        // Act
        final var actual = service.findAllPrometheus();

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(servers));
        verify(repository, times(1)).findByAdministrationPrometheusIsNotNull();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_return_prometheus_sd_list(){
        // Arrange
        final var servers = Faker.listServer();
        when(repository.findByAdministrationPrometheusIsNotNull()).thenReturn(Optional.of(servers));

        // Act
        final var actual = service.getPrometheusSD();

        // Assert
        assertThat(actual.size()).isEqualTo(servers.size());
        verify(repository, times(1)).findByAdministrationPrometheusIsNotNull();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_return_prometheus_sd_empty_list(){
        // Arrange
        when(repository.findByAdministrationPrometheusIsNotNull()).thenReturn(Optional.empty());

        // Act
        final var actual = service.getPrometheusSD();

        // Assert
        assertThat(actual).isEqualTo(new ArrayList<>());
        verify(repository, times(1)).findByAdministrationPrometheusIsNotNull();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_not_found_a_server_that_doesnt_exist() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        final var actual = service.find(Faker.faker.random().nextLong());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.empty());
        verify(repository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_save_one_server(){
        // Arrange
        final var serverToSave = Faker.server();
        when(repository.save(any(Server.class))).thenReturn(serverToSave);

        // Act
        final var actual = service.create(new Server());

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(serverToSave);
        verify(repository, times(1)).save(any(Server.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void should_update_one_server_that_exist(){
        // Arrange
        final var server = Faker.server();
        final var updatedServer = Faker.server();
        updatedServer.setId(server.getId());
        when(repository.findById(server.getId())).thenReturn(Optional.of(server));
        when(repository.save(any(Server.class))).thenReturn(updatedServer);

        // Act
        final var actual = service.update(server.getId(), updatedServer);

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(updatedServer));
        assertThat(actual).usingRecursiveComparison().isNotEqualTo(Optional.of(server));
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any(Server.class));
    }

    @Test
    public void should_not_update_a_server_that_doesnt_exist(){
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        final var updateServer = Faker.server();
        final var actual = service.update(updateServer.getId(), updateServer);

        // Arrange
        assertThat(actual).isEqualTo(Optional.empty());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any(Server.class));
    }

    @Test
    public void should_delete_one_server(){
        // Arrange
        doNothing().when(repository).deleteById(anyLong());

        // Act & Assert
        service.delete(Faker.faker.random().nextLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}
