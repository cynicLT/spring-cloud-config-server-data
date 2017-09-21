package org.springframework.cloud.config.server.data.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.config.server.data.environment.DataEnvironment;
import org.springframework.cloud.config.server.data.repository.DataRepository;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class DataConfigServerConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "dataRepository")
    DataRepository emptyDataRepository() {
        return (application, profiles, labels) -> new ArrayList<>();
    }

    @Bean
    EnvironmentRepository environmentRepository(DataRepository dataRepository) {
        return new DataEnvironment(dataRepository);
    }
}
