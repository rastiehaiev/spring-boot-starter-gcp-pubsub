package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Topic;
import com.sbrati.telegram.domain.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.PubSubAdmin;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.util.Assert;

@Slf4j
public abstract class AbstractGcpPubSubPublisher<T> {

    @Autowired
    protected PubSubTemplate template;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private PubSubAdmin pubSubAdmin;

    @SneakyThrows
    protected void publish(Event<T> payload, String topicName) {
        byte[] bytes = objectMapper.writeValueAsBytes(payload);
        ByteString data = ByteString.copyFrom(bytes);
        template.publish(topicName, PubsubMessage.newBuilder().setData(data).build());
    }

    protected void initializeTopic(String topicName) {
        Assert.hasText(topicName, "topicName cannot be null or empty.");
        try {
            log.info("Creating Pub/Sub topic {}.", topicName);
            Topic topic = pubSubAdmin.createTopic(topicName);
            log.info("Created Pub/Sub topic: {}.", topic);
        } catch (AlreadyExistsException ignore) {
            log.info("Topic {} already exists.", topicName);
        }
    }
}
