package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.sbrati.telegram.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
public class GcpPubSubPublisher<T> extends AbstractGcpPubSubPublisher<T> {

    private final String topicName;

    public void publish(Event<T> payload) {
        publish(payload, topicName);
    }

    @PostConstruct
    private void initializeTopic() {
        super.initializeTopic(topicName);
    }
}
