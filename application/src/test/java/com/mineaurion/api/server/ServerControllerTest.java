package com.mineaurion.api.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mineaurion.api.Faker;
import com.mineaurion.api.library.model.prometheus.Labels;
import com.mineaurion.api.library.model.prometheus.PrometheusSD;
import com.mineaurion.api.server.model.Server;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ServerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServerService serverService;

    // Used for converting server to/from JSON
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getAllQueryWithoutQueryParams() throws Exception {
        // Arrange
        when(serverService.findAll(Sort.Direction.ASC, "id")).thenReturn(Faker.listServer());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/servers").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andReturn();

        // Assert
        verify(serverService, times(1)).findAll(Sort.Direction.ASC, "id");
    }

    @Test
    public void getAllQueryWithQueryParams() throws Exception {
        // Arrange
        String directionQueryParam = "ASC";
        Sort.Direction direction = Sort.Direction.valueOf(directionQueryParam);
        String field = "name";
        when(serverService.findAll(direction, field)).thenReturn(Faker.listServer());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/servers")
                .param("sortOrder", directionQueryParam)
                .param("sortField", field)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Assert
        verify(serverService, times(1)).findAll(direction, field);
    }

    @Test
    public void getAllQueryWithBadSortOrder() throws Exception {
        // Act
        String directionQueryParam = "BAD";
        String field = "name";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/servers")
                .param("sortOrder", directionQueryParam)
                .param("sortField", field)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        verify(serverService, times(0)).findAll(Sort.Direction.ASC, "name");
    }

    @Test
    public void getAllQueryWithEmptyDatabase() throws Exception {
        // Arrange
        String directionQueryParam = "ASC";
        Sort.Direction direction = Sort.Direction.valueOf(directionQueryParam);
        String field = "name";
        when(serverService.findAll(direction, field)).thenReturn(List.of());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/servers")
                .param("sortOrder", directionQueryParam)
                .param("sortField", field)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();


        // Assert
        verify(serverService, times(1)).findAll(direction, field);
    }

    @Test
    public void getOneQueryExist() throws Exception {
        // Arrange
        Server server = Faker.server();
        Long id = server.getId();
        when(serverService.find(id)).thenReturn(Optional.of(server));

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(String.format("/servers/%s", id)).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(id)))
                .andReturn();

        // Assert
        verify(serverService, times(1)).find(id);
    }

    @Test
    public void getOneQueryNotExist() throws Exception {
        // Arrange
        Long id = 12345678910L;
        when(serverService.find(id)).thenReturn(Optional.empty());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(String.format("/servers/%s", id)).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
        // Assert
        verify(serverService, times(1)).find(id);
    }

    @Test
    public void getOneByExternalIdExist() throws Exception {
        // Arrange
        Server server = Faker.server();
        String id = server.getAdministration().getExternalId().toString();
        when(serverService.findByExternalId(id)).thenReturn(Optional.of(server));

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(String.format("/servers/externalid/%s", id)).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.administration.externalId", is(id)))
                .andReturn();

        // Assert
        verify(serverService, times(1)).findByExternalId(id);
    }

    @Test
    public void getOneByExternalIdNotExist() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        when(serverService.findByExternalId(id)).thenReturn(Optional.empty());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(String.format("/servers/externalid/%s", id)).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        verify(serverService, times(1)).findByExternalId(id);
    }

    @Test
    public void getPrometheusServiceDiscovery() throws Exception {
        // Arrange
        List<PrometheusSD> mockPrometheusSd = List.of(
                new PrometheusSD(
                        IntStream.rangeClosed(1, 10).mapToObj(i -> Faker.faker.internet().ipV4Address()).collect(Collectors.toList()),
                        new Labels(Faker.faker.name().username(), Faker.semver(), Faker.faker.name().title())
                )
        );
        when(serverService.getPrometheusSD()).thenReturn(mockPrometheusSd);

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/servers/prometheus-sd").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Assert
        verify(serverService, times(1)).getPrometheusSD();
    }

    @Test
    public void createServer() throws Exception {
        // Arrange
        Server server = Faker.server();
        byte[] mockServerJson = toJson(server);
        when(serverService.create(any(Server.class))).thenReturn(server);

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/servers")
                .content(mockServerJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "http://localhost/servers/" + server.getId().toString()))
                .andReturn();

        // Assert
        verify(serverService, times(1)).create(any(Server.class));
    }

    @Test
    public void createServerWithBadJson() throws Exception {
        // Arrange
        Server server = Faker.server();
        String json = "{ \"badKey\": \"badValue\" }";
        when(serverService.create(any(Server.class))).thenReturn(server);

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/servers")
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();

        // Assert
        verify(serverService, times(0)).create(any(Server.class));
    }

    @Test
    public void update() throws Exception {
        // Arrange
        // Create server before
        Server server = Faker.server();
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/servers")
                .content(toJson(server))
                .accept(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        Server mockServerUpdate = Faker.server();
        mockServerUpdate.setId(server.getId());

        byte[] mockServerUpdateJson = toJson(mockServerUpdate);

        when(serverService.create(any(Server.class))).thenReturn(server);
        when(serverService.update(eq(mockServerUpdate.getId()), any(Server.class))).thenReturn(Optional.of(mockServerUpdate));

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/servers/" + mockServerUpdate.getId().toString())
                .content(mockServerUpdateJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        verify(serverService, times(0)).create(server);
        verify(serverService, times(1)).update(eq(mockServerUpdate.getId()), any(Server.class));
    }

    @Test
    public void updateServerWithBadJson() throws Exception {
        // Arrange
        Server server = Faker.server();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/servers")
                        .content(toJson(server))
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        String mockServerUpdateJson = "{ \"badKey\": \"badValue\" }";

        when(serverService.create(any(Server.class))).thenReturn(server);
        when(serverService.update(eq(server.getId()), any(Server.class))).thenReturn(Optional.of(server));

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/servers/" + server.getId().toString())
                .content(mockServerUpdateJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();

        // Assert
        verify(serverService, times(0)).create(server);
        verify(serverService, times(0)).update(eq(server.getId()), any(Server.class));
    }

    @Test
    public void updateInsert() throws Exception {
        // Arrange
        Server server = Faker.server();
        byte[] mockServerUpdateJson = toJson(server);

        when(serverService.create(any(Server.class))).thenReturn(server);
        when(serverService.update(eq(server.getId()), any(Server.class))).thenReturn(Optional.empty());

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/servers/" + server.getId().toString())
                .content(mockServerUpdateJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        verify(serverService, times(1)).create(any(Server.class));
        verify(serverService, times(1)).update(eq(server.getId()), any(Server.class));
    }

    @Test
    public void delete() throws Exception {
        // Arrange
        Server server = Faker.server();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/servers")
                        .content(toJson(server))
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/servers/" + server.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        // Assert
        verify(serverService, times(1)).delete(server.getId());
    }

    private byte[] toJson(Object object) throws Exception {
        return this.mapper.writeValueAsString(object).getBytes();
    }
}
