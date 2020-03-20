package com.sbrati.spring.boot.starter.gcp.pubsub;

import lombok.Data;

import java.util.List;

@Data
public class GoogleCloudSubscribersConfig {

    private List<GoogleCloudPubSubSubscription> subscriptions;
}
