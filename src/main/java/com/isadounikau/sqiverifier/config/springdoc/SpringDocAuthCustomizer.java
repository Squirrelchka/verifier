package com.isadounikau.sqiverifier.config.springdoc;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

public class SpringDocAuthCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (handlerMethod.hasMethodAnnotation(OpenApiAvailable.class)) {
            return operation;
        } else {
            return null;
        }
    }
}
