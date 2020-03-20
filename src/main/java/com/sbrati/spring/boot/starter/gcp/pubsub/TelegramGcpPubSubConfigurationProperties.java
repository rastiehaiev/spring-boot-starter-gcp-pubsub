package com.sbrati.spring.boot.starter.gcp.pubsub;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "google-cloud.pub-sub")
public class TelegramGcpPubSubConfigurationProperties {

    private GoogleCloudPublishersConfig publish;
    private GoogleCloudSubscribersConfig subscribe;
}