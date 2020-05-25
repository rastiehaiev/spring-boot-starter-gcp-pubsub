package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.pubsub.v1.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.PubSubAdmin;
import org.springframework.util.Assert;

@Slf4j
class Utils {

    static void initializeTopic(PubSubAdmin pubSubAdmin, String topicName) {
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
