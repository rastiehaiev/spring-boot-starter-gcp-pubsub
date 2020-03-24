package com.sbrati.spring.boot.starter.gcp.pubsub;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;

@Slf4j
@RequiredArgsConstructor
public class GcpProjectIdProviderImpl implements GcpProjectIdProvider {

    @NonNull
    private final String projectId;

    @Override
    public String getProjectId() {
        return projectId;
    }
}
