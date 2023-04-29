package com.mineaurion.api.query;

import com.mineaurion.api.Faker;
import com.mineaurion.api.query.lib.MCQueryResponse;
import com.mineaurion.api.server.ServerService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class QueryServiceTest {

    @Mock
    public ServerService serverService;

    @Mock
    public MinecraftQueryService minecraftQueryService;

    @Mock
    public PterodactylQueryService pterodactylQueryService;

    @InjectMocks
    private QueryService service;

    @Test
    public void should_find_all_query_server_with_default_arg(){
        // Arrange
        List<com.mineaurion.api.server.model.Server> servers = Faker.listServer();
        when(serverService.findAll(any(Sort.Direction.class), anyString(), anyBoolean())).thenReturn(servers);
        when(minecraftQueryService.getQueryResponse(anyString(), anyString(), anyInt())).thenReturn(QueryFaker.mcQuery(true));
        when(pterodactylQueryService.getNextRebootSchedule(any())).thenReturn(Optional.empty());

        // Act
        final var actual = service.findAll();

        // Assert
        assertEquals(servers.size(), actual.size());
        verify(minecraftQueryService, times(servers.size())).getQueryResponse(anyString(), anyString(), anyInt());
        verify(serverService, times(1)).findAll(Sort.Direction.ASC, "id", false);
        verify(pterodactylQueryService, times(servers.size())).getNextRebootSchedule(any());
    }

    @Test
    public void should_find_all_query_server_with_custom_direction_and_field(){
        // Arrange
        List<com.mineaurion.api.server.model.Server> servers = Faker.listServer();
        when(serverService.findAll(any(Sort.Direction.class), anyString(), anyBoolean())).thenReturn(servers);
        when(minecraftQueryService.getQueryResponse(anyString(), anyString(), anyInt())).thenReturn(QueryFaker.mcQuery(true));
        when(pterodactylQueryService.getNextRebootSchedule(any())).thenReturn(Optional.empty());

        // Act
        Sort.Direction sortDirection = Sort.Direction.DESC;
        String sortField = "name";
        final var actual = service.findAll(sortDirection, sortField);

        // Assert
        assertEquals(servers.size(), actual.size());
        verify(minecraftQueryService, times(servers.size())).getQueryResponse(anyString(), anyString(), anyInt());
        verify(serverService, times(1)).findAll(sortDirection, sortField, false);
        verify(pterodactylQueryService, times(servers.size())).getNextRebootSchedule(any());
    }

    @Test
    public void should_find_one_by_dns(){
        // Arrange
        com.mineaurion.api.server.model.Server server = Faker.server();
        when(serverService.findByDns(anyString())).thenReturn(Optional.of(server));
        when(minecraftQueryService.getQueryResponse(anyString(), anyString(), anyInt())).thenReturn(QueryFaker.mcQuery(server,true));
        when(pterodactylQueryService.getNextRebootSchedule(any())).thenReturn(Optional.empty());

        // Act
        final var actual = service.findOneByDns(server.getDns());

        // Assert
        assertThat(actual).isNotEqualTo(Optional.empty());
        verify(serverService, times(1)).findByDns(server.getDns());
        verify(minecraftQueryService, times(1)).getQueryResponse(anyString(), anyString(), anyInt());
        verify(pterodactylQueryService, times(1)).getNextRebootSchedule(any());
    }

    @Test
    public void should_get_schedule_in_timestamp_when_pterodactyl_respond(){
        // Arrange
        com.mineaurion.api.server.model.Server server = Faker.server();
        final var currentDate = new Date();
        when(serverService.findByDns(anyString())).thenReturn(Optional.of(server));
        when(minecraftQueryService.getQueryResponse(anyString(), anyString(), anyInt())).thenReturn(QueryFaker.mcQuery(server,true));
        when(pterodactylQueryService.getNextRebootSchedule(any())).thenReturn(Optional.of(currentDate));

        // Act
        final var actual = service.findOneByDns(server.getDns());

        // Assert
        assertThat(actual).isNotEqualTo(Optional.empty());
        assertThat(actual.get().getSchedule().getnextReboot()).isEqualTo(currentDate.getTime() / 1000);
        verify(serverService, times(1)).findByDns(server.getDns());
        verify(minecraftQueryService, times(1)).getQueryResponse(anyString(), anyString(), anyInt());
        verify(pterodactylQueryService, times(1)).getNextRebootSchedule(any());
    }

    @Test
    public void should_get_player_count_for_all_servers(){
        // Arrange
        List<com.mineaurion.api.server.model.Server> servers = Faker.listServer();
        MCQueryResponse queryServer = QueryFaker.mcQuery(false);
        when(serverService.findAll(any(Sort.Direction.class), anyString(), anyBoolean())).thenReturn(servers);
        when(minecraftQueryService.getQueryResponse(anyString(), anyString(), anyInt())).thenReturn(queryServer);
        when(pterodactylQueryService.getNextRebootSchedule(any())).thenReturn(Optional.empty());

        // Act
        Integer expectedTotalPlayer = queryServer.getMaxPlayers() * servers.size();
        final var actual = service.getPlayerCount();

        // Assert
        assertEquals(expectedTotalPlayer, actual);
        verify(minecraftQueryService, times(servers.size())).getQueryResponse(anyString(), anyString(), anyInt());
        verify(serverService, times(1)).findAll(Sort.Direction.ASC, "id", false);
    }
}
