package com.sbrati.spring.boot.starter.gcp.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(TelegramGcpConfigurationProperties.class)
public class TelegramGcpConfiguration {

    private final TelegramGcpConfigurationProperties properties;

    @Bean
    @Profile("development")
    public GcpProjectIdProvider gcpProjectIdProvider() {
        log.info("Created GcpProjectIdProvider for development profile.");
        return new GcpProjectIdProviderImpl(properties.getProjectId());
    }
}
