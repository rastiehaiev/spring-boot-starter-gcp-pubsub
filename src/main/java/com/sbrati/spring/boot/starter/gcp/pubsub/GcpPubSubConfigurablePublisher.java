package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.sbrati.telegram.domain.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class GcpPubSubConfigurablePublisher<T> extends AbstractGcpPubSubPublisher<T> {

    @Autowired
    private TelegramGcpPubSubConfigurationProperties properties;

    private final String topicKey;

    private String topicName;

    public void publish(Event<T> payload) {
        publish(payload, topicName);
    }

    @PostConstruct
    private void initializeTopicNameAndCreate() {
        this.topicName = properties.getPublish().getTopics().stream()
                .filter(topicSpec -> topicSpec.getKey().equals(topicKey))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find topic by key " + topicKey))
                .getName();
        initializeTopic(topicName);
    }
}
