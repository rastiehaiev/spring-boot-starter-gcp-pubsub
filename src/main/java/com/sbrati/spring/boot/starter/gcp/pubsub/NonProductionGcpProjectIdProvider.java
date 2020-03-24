package com.sbrati.spring.boot.starter.gcp.pubsub;

import com.google.api.client.util.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!production")
public class NonProductionGcpProjectIdProvider implements GcpProjectIdProvider {

    public NonProductionGcpProjectIdProvider() {
        log.info("Created GcpProjectIdProvider for non-production profile.");
    }

    @Value("${google-cloud.project-id}")
    private String projectId;

    @Override
    public String getProjectId() {
        return projectId;
    }
}
