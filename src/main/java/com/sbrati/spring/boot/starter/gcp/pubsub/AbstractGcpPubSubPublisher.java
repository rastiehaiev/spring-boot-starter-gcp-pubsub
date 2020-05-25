package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.sbrati.telegram.domain.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.PubSubAdmin;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;

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
        Utils.initializeTopic(pubSubAdmin, topicName);
    }
}
