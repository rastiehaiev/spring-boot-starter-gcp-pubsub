package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.sbrati.telegram.domain.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class GcpPubSubSubscriber<T> {

    private final Class<T> type;
    private final String subscriptionKey;

    @Getter
    private String subscription;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TelegramGcpPubSubConfigurationProperties properties;

    public abstract void process(Event<T> event);

    protected Event<T> convertToEvent(PubsubMessage message) {
        ByteString data = message.getData();
        if (data == null) {
            return null;
        }
        String valueAsString = data.toStringUtf8();
        try {
            Event<Map<String, Object>> mapEvent = objectMapper.readValue(valueAsString, new TypeReference<Event<Map<String, Object>>>() {
            });
            Map<String, Object> payload = mapEvent.getPayload();
            T typedPayload = objectMapper.convertValue(payload, type);
            return mapEvent.with(typedPayload);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert event {}", valueAsString, e);
        }
        return null;
    }

    @PostConstruct
    private void initializeSubscription() {
        List<GoogleCloudPubSubSubscription> subscriptions = properties.getSubscribe().getSubscriptions();
        subscription = subscriptions.stream().filter(subscription -> subscription.getKey().equals(subscriptionKey))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find subscription by key " + subscriptionKey))
                .getName();
    }
}
