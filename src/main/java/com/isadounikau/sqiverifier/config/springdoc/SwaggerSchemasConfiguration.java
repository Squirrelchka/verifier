package com.isadounikau.sqiverifier.config.springdoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerSchemasConfiguration {

    private final Logger log = LoggerFactory.getLogger(SwaggerSchemasConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(name = "openAPIManagementGroupedOpenAPI")
    public GroupedOpenApi sqlTaskManagementGroupedOpenAPI() {
        log.debug("Initializing OpenApi Sql Task Schema group");
        GroupedOpenApi.Builder builder = GroupedOpenApi.builder()
            .group("sqlTaskManagement");
        builder.addOperationCustomizer(new SpringDocAuthCustomizer());
        return builder.build();
    }

}
