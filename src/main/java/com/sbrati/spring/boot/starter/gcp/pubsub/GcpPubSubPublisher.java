package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.sbrati.telegram.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
public class GcpPubSubPublisher<T> {

    private final String topicKey;

    private String topicName;

    @Autowired
    private PubSubTemplate template;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TelegramGcpPubSubConfigurationProperties properties;

    public void publish(Event<T> payload) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(payload);
            ByteString data = ByteString.copyFrom(bytes);
            template.publish(topicName, PubsubMessage.newBuilder().setData(data).build());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    @PostConstruct
    private void initializeTopicName() {
        topicName = properties.getPublish().getTopics().stream()
                .filter(topicSpec -> topicSpec.getKey().equals(topicKey))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find topic by key " + topicKey))
                .getName();
        log.info("Created publisher for topic [{}].", topicName);
    }
}
