package com.mineaurion.api.query;

import com.mineaurion.api.Faker;
import com.mineaurion.api.library.model.query.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = QueryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    @Test
    public void getAllQueryWithoutQueryParams() throws Exception {
        // Arrange
        when(queryService.findAll(Sort.Direction.ASC, "id")).thenReturn(Faker.listQueryServer());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/query").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Assert
        verify(queryService, times(1)).findAll(Sort.Direction.ASC, "id");
    }

    @Test
    public void getAllQueryWithQueryParams() throws Exception {
        // Arrange
        String directionQueryParam = "ASC";
        Sort.Direction direction = Sort.Direction.valueOf(directionQueryParam);
        String field = "name";
        when(queryService.findAll(direction, field)).thenReturn(Faker.listQueryServer());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/query")
                .param("sortOrder", directionQueryParam)
                .param("sortField", field)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Assert
        verify(queryService, times(1)).findAll(direction, field);
    }

    @Test
    public void getAllQueryWithBadSortOrder() throws Exception {
        // Act
        String directionQueryParam = "BAD";
        String field = "name";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/query")
                .param("sortOrder", directionQueryParam)
                .param("sortField", field)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        verify(queryService, times(0)).findAll(Sort.Direction.ASC, "name");
    }

    @Test
    public void getAllQueryWithEmptyDatabase() throws Exception {
        // Arrange
        String directionQueryParam = "ASC";
        Sort.Direction direction = Sort.Direction.valueOf(directionQueryParam);
        String field = "name";
        when(queryService.findAll(direction, field)).thenReturn(List.of());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/query")
                .param("sortOrder", directionQueryParam)
                .param("sortField", field)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();

        // Assert
        verify(queryService, times(1)).findAll(direction, field);
    }

    @Test
    public void getOneQueryExist() throws Exception {
        // Arrange
        Server server = Faker.queryServer();
        String dns = server.getDns();
        when(queryService.findOneByDns(dns)).thenReturn(Optional.of(server));

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(String.format("/query/%s", dns)).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.dns", is(dns)))
                .andReturn();

        // Assert
        verify(queryService, times(1)).findOneByDns(dns);
    }

    @Test
    public void getOneQueryNotExist() throws Exception {
        // Arrange
        String dns = "dns.notexist.com";
        when(queryService.findOneByDns(dns)).thenReturn(Optional.empty());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(String.format("/query/%s", dns)).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        verify(queryService, times(1)).findOneByDns(dns);
    }

    @Test
    public void getOnlinePlayers() throws Exception {
        // Arrange
        Integer nbPlayers = 10;
        when(queryService.getPlayerCount()).thenReturn(nbPlayers);

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/query/online-players").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.onlinePlayers", is(nbPlayers)))
                .andReturn();

        // Assert
        verify(queryService, times(1)).getPlayerCount();
    }
}
