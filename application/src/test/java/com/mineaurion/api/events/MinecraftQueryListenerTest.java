package com.mineaurion.api.events;


import com.mineaurion.api.discord.Webhook;
import com.mineaurion.api.events.minecraftquery.MinecraftQueryErrorEvent;
import com.mineaurion.api.events.minecraftquery.MinecraftQuerySuccessEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@ActiveProfiles("test")
public class MinecraftQueryListenerTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Mock
    Environment env;

    @SpyBean
    private MinecraftQueryListener minecraftQueryListener;

    @Mock
    private Webhook webhook;

    @BeforeEach
    public void initialize() throws NoSuchFieldException {
        when(env.getProperty("discord.webhook.message")).thenReturn("<@103961433351389184> The server %s could not be contacted for 20 minutes");
        when(env.getProperty("discord.webhook")).thenReturn("https://discord.com/api/webhooks/");
        // Field errorServerField = minecraftQueryListener.getClass().getDeclaredField("errorServer");
        // errorServerField.setAccessible(true);
        // ReflectionTestUtils.setField(service, "applicationEventPublisher", applicationEventPublisher);
    }

    @Test
    public void should_do_nothing_if_event_key_is_not_in_errorServer_when_success() {
        // Arrange
        MinecraftQuerySuccessEvent minecraftQuerySuccessEvent = new MinecraftQuerySuccessEvent(new Object(), "test", "test", 123);

        // Act
        publisher.publishEvent(minecraftQuerySuccessEvent);

        // Assert
        verify(minecraftQueryListener, times(1)).handleMinecraftServerSuccessEvent(minecraftQuerySuccessEvent);
        assertThat(minecraftQueryListener.errorServer.size()).isEqualTo(0);

    }

    @Test
    public void should_remove_key_if_in_errorServer_when_success(){
        // Arrange
        MinecraftQuerySuccessEvent minecraftQuerySuccessEvent = new MinecraftQuerySuccessEvent(new Object(), "test", "test", 123);
        minecraftQueryListener.errorServer.put(minecraftQuerySuccessEvent.getMapKey(), 2);
        //
        assertThat(minecraftQueryListener.errorServer.size()).isEqualTo(1);

        // Act
        publisher.publishEvent(minecraftQuerySuccessEvent);

        // Assert
        verify(minecraftQueryListener, times(1)).handleMinecraftServerSuccessEvent(minecraftQuerySuccessEvent);
        assertThat(minecraftQueryListener.errorServer.size()).isEqualTo(0);
    }

    @Test
    public void should_add_element_and_increment_if_exist_to_errorServer_when_error(){
        // Arrange
        MinecraftQueryErrorEvent minecraftQueryErrorEvent = new MinecraftQueryErrorEvent(new Object(), "test", "test", 123, "Error");

        // Act
        publisher.publishEvent(minecraftQueryErrorEvent);

        // Assert
        verify(minecraftQueryListener, times(1)).handleMinecraftServerErrorEvent(minecraftQueryErrorEvent);
        assertThat(minecraftQueryListener.errorServer.size()).isEqualTo(1);

        // Act again
        publisher.publishEvent(minecraftQueryErrorEvent);

        // Assert Again
        verify(minecraftQueryListener, times(2)).handleMinecraftServerErrorEvent(minecraftQueryErrorEvent);
        assertThat(minecraftQueryListener.errorServer.size()).isEqualTo(1);
        assertThat(minecraftQueryListener.errorServer.get(minecraftQueryErrorEvent.getMapKey())).isEqualTo(2);
    }

    @Test
    public void should_send_webhook_with_errorCount_equal_maxErrorCount_when_error() throws IOException {
        // Arrange
        MinecraftQueryErrorEvent minecraftQueryErrorEvent = new MinecraftQueryErrorEvent(new Object(), "test", "test", 123, "Error");
        Integer maxErrorCount = 5 + 1;

        // Act
        for (int i = 0; i < maxErrorCount; i++) {
            publisher.publishEvent(minecraftQueryErrorEvent);
        }

        // Assert
        verify(minecraftQueryListener, times(maxErrorCount)).handleMinecraftServerErrorEvent(minecraftQueryErrorEvent);
        assertThat(minecraftQueryListener.errorServer.size()).isEqualTo(1);
        assertThat(minecraftQueryListener.errorServer.get(minecraftQueryErrorEvent.getMapKey())).isGreaterThanOrEqualTo(maxErrorCount);

    }
}
