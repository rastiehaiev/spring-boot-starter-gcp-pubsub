package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.sbrati.telegram.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class GcpPubSubMultiTopicPublisher<T> extends AbstractGcpPubSubPublisher<T> {

    private final Map<String, String> topics = new ConcurrentHashMap<>();

    public void publish(Event<T> payload, String topicName) {
        topics.computeIfAbsent(topicName, this::createTopicIfDoesNotExist);
        super.publish(payload, topicName);
    }

    private String createTopicIfDoesNotExist(String key) {
        super.initializeTopic(key);
        return key;
    }
}
