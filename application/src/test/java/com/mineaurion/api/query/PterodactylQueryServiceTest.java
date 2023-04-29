package com.mineaurion.api.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mineaurion.api.Faker;
import com.mineaurion.api.query.pterodactyl.schedule.ScheduleResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PterodactylQueryServiceTest {

    @InjectMocks
    private PterodactylQueryService service;

    @Mock
    Environment env;

    private final ObjectMapper mapper = new ObjectMapper();

    public static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    public void initialize(){
        when(env.getProperty("pterodactyl.api_url")).thenReturn("http://localhost:" + mockWebServer.getPort());
        when(env.getProperty("pterodactyl.api_token")).thenReturn("mineaurion");
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void should_get_next_reboot_with_successful_response_on_pterodactyl_api() throws Exception {
        // Arrange
        ScheduleResponse scheduleResponse = Faker.scheduleResponse(2);
        mockWebServer.enqueue(
                new MockResponse()
                    .setBody(mapper.writeValueAsString(scheduleResponse))
                        .setHeader("Content-Type", "application/json")
        );

       // Act
        final var actual = service.getNextRebootSchedule(UUID.randomUUID());

        // Assert
        assertThat(actual).isPresent();
        assertThat(actual).isEqualTo(Optional.of(scheduleResponse.getData().get(2).getAttributes().getNext_run_at()));
    }

    @Test
    public void should_get_empty_next_reboot_with_unsuccessful_response_on_pterodactyl_api() {
        // Arrange
        ScheduleResponse scheduleResponse = Faker.scheduleResponse(2);
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setHeader("Content-Type", "application/json")
        );

        // Act
        final var actual = service.getNextRebootSchedule(UUID.randomUUID());

        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    public void should_get_empty_next_reboot_with_successful_response_with_no_reboot_schedule_on_pterodactyl_api() throws Exception {
        // Arrange
        ScheduleResponse scheduleResponse = Faker.scheduleResponse(2);
        scheduleResponse.getData().forEach(schedule -> schedule.getAttributes().setName("nothing"));
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(mapper.writeValueAsString(scheduleResponse))
                        .setHeader("Content-Type", "application/json")
        );

        // Act
        final var actual = service.getNextRebootSchedule(UUID.randomUUID());

        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    public void should_get_empty_next_reboot_with_successful_response_with_no_body_on_pterodactyl_api() {
        // Arrange
        ScheduleResponse scheduleResponse = Faker.scheduleResponse(2);
        scheduleResponse.getData().forEach(schedule -> schedule.getAttributes().setName("nothing"));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "application/json")
        );

        // Act
        final var actual = service.getNextRebootSchedule(UUID.randomUUID());

        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    public void should_get_empty_next_reboot_with_successful_response_with_no_schedule_pterodactyl_api() throws Exception {
        // Arrange
        ScheduleResponse scheduleResponse = Faker.scheduleResponse(2);
        scheduleResponse.setAttributes(new ArrayList<>());
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(mapper.writeValueAsString(scheduleResponse))
                        .setHeader("Content-Type", "application/json")
        );

        // Act
        final var actual = service.getNextRebootSchedule(UUID.randomUUID());

        // Assert
        assertThat(actual).isEmpty();
    }
}
