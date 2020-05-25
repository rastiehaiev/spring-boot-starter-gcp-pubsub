package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.sbrati.telegram.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.cloud.gcp.pubsub.enabled", matchIfMissing = true)
@EnableConfigurationProperties(TelegramGcpPubSubConfigurationProperties.class)
public class TelegramGcpPubSubConfiguration {

    private final PubSubTemplate template;

    private List<GcpPubSubSubscriber<?>> subscribers;

    @SuppressWarnings("unchecked")
    @EventListener(ApplicationReadyEvent.class)
    public void subscribe() {
        if (subscribers != null) {
            for (GcpPubSubSubscriber subscriber : subscribers) {
                String subscription = subscriber.getSubscriptionName();
                template.subscribe(subscription, ackPubsubMessage -> {
                    try {
                        Event<?> event = subscriber.convertToEvent(ackPubsubMessage.getPubsubMessage());
                        if (event != null) {
                            subscriber.process(event);
                        } else {
                            log.warn("Null payload received: {}.", ackPubsubMessage.getPubsubMessage());
                        }
                    } catch (Exception e) {
                        log.error("Error while processing message {}.", ackPubsubMessage, e);
                    } finally {
                        ackPubsubMessage.ack();
                    }
                });
                log.info("Subscribed to [{}].", subscription);
            }
        }
    }

    @Autowired(required = false)
    public void setSubscribers(List<GcpPubSubSubscriber<?>> subscribers) {
        this.subscribers = subscribers;
    }
}
