package com.mineaurion.api.query;

import com.mineaurion.api.events.MinecraftQueryListener;
import com.mineaurion.api.events.minecraftquery.MinecraftQueryErrorEvent;
import com.mineaurion.api.events.minecraftquery.MinecraftQuerySuccessEvent;
import com.mineaurion.api.query.lib.MCQuery;
import com.mineaurion.api.query.lib.MCQueryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MinecraftQueryServiceTest {

    @InjectMocks
    private MinecraftQueryService service;

    @Mock
    private MCQuery mcQuery;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private MinecraftQueryListener minecraftQueryListener;

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(service, "applicationEventPublisher", applicationEventPublisher);
    }

    @Test
    public void should_return_mcquery_and_send_event_when_success() throws IOException {
        // Arrange
        MCQueryResponse mcQueryResponse = new MCQueryResponse();
        mcQueryResponse.setStatus(true).setMaxPlayers(10).setOnlinePlayers(10);
        doNothing().when(applicationEventPublisher).publishEvent(any());
        when(mcQuery.sendQueryRequest(anyString(), anyInt())).thenReturn(mcQueryResponse);

        // Act
        final var actual = service.getQueryResponse("test", "test", 123);

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(mcQueryResponse);
        verify(mcQuery, times(1)).sendQueryRequest("test", 123);
        verify(applicationEventPublisher, times(1)).publishEvent(any(MinecraftQuerySuccessEvent.class));
    }

    @Test
    public void should_return_default_mcquery_and_send_event_when_error() throws IOException {
        // Arrange
        MCQueryResponse mcQueryResponse = new MCQueryResponse();
        doNothing().when(applicationEventPublisher).publishEvent(any());
        when(mcQuery.sendQueryRequest(anyString(), anyInt())).thenThrow(IOException.class);

        // Act
        final var actual = service.getQueryResponse("test", "test", 123);

        // Assert

        assertThat(actual).usingRecursiveComparison().isEqualTo(mcQueryResponse);
        verify(mcQuery, times(1)).sendQueryRequest("test", 123);
        verify(applicationEventPublisher, times(1)).publishEvent(any(MinecraftQueryErrorEvent.class));
    }
}
