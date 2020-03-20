package com.sbrati.spring.boot.starter.gcp.pubsub;

import lombok.Data;

import java.util.List;

@Data
public class GoogleCloudPublishersConfig {

    private List<GoogleCloudPubSubTopic> topics;
}
