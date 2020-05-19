package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Topic;
import com.sbrati.telegram.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.util.Assert;

@Slf4j
public abstract class AbstractGcpPubSubPublisher<T> {

    @Autowired
    protected PubSubTemplate template;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private TopicAdminClient topicAdminClient;

    protected void publish(Event<T> payload, String topicName) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(payload);
            ByteString data = ByteString.copyFrom(bytes);
            template.publish(topicName, PubsubMessage.newBuilder().setData(data).build());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected void initializeTopic(String topicName) {
        Assert.hasText(topicName, "topicName cannot be null or empty.");
        Topic topic = topicAdminClient.getTopic(topicName);
        if (topic == null) {
            topicAdminClient.createTopic(topicName);
            log.info("Created new topic [{}].", topicName);
        }
        log.info("Created publisher for topic [{}].", topicName);
    }
}
