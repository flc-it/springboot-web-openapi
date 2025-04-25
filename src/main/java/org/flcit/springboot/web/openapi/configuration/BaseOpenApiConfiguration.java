/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flcit.springboot.web.openapi.configuration;

import java.util.Optional;
import java.util.function.Consumer;

import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.async.WebAsyncTask;

import org.flcit.commons.core.util.ClassUtils;
import org.flcit.springboot.web.openapi.util.OpenApiUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public abstract class BaseOpenApiConfiguration extends OpenAPI {

    static {
        SpringDocUtils.getConfig().addResponseWrapperToIgnore(WebAsyncTask.class);
        SpringDocUtils.getConfig().addResponseWrapperToIgnore(Optional.class);
        final Class<?> wrapperValid = ClassUtils.find("org.flcit.springboot.web.core.domain.WrapperValid");
        if (wrapperValid != null) {
            SpringDocUtils.getConfig().addResponseWrapperToIgnore(wrapperValid);
        }
    }

    /**
     * @return
     */
    public abstract boolean isOnlyDocs();

    @SafeVarargs
    protected static final OpenApiCustomiser globalResponses(boolean secured, Consumer<Operation>... consumers) {
        return new OpenApiCustomiser() {
            @SuppressWarnings("rawtypes")
            @Override
            public void customise(OpenAPI openApi) {
                final Schema apiErrorBase = OpenApiUtils.getOrAddSchemas(openApi, ClassUtils.find("org.flcit.springboot.web.error.domain.ApiErrorBase"));
                final Schema apiErrors = OpenApiUtils.getOrAddSchemas(openApi, ClassUtils.find("org.flcit.springboot.web.error.domain.ApiErrors"));
                final Schema apiErrorTrace = OpenApiUtils.getOrAddSchemas(openApi, ClassUtils.find("org.flcit.springboot.web.error.domain.ApiErrorTrace"));
                for (PathItem pathItem : openApi.getPaths().values()) {
                    for (Operation operation: pathItem.readOperations()) {
                        if (consumers != null) {
                            for (Consumer<Operation> consumer : consumers) {
                                consumer.accept(operation);
                            }
                        }
                        setAllMediaType(operation);
                        setResponsesErrors(operation, secured, apiErrorBase, apiErrors, apiErrorTrace);
                    }
                }
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private static final void setResponsesErrors(Operation operation, boolean secured, Schema apiErrorBase, Schema apiErrors, Schema apiErrorTrace) {
        ApiResponses apiResponses = operation.getResponses();
        if (apiErrors != null) {
            apiResponses.addApiResponse("400", OpenApiUtils.createApiResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), apiErrors));
        }
        if (secured) {
            apiResponses.addApiResponse("401", OpenApiUtils.createApiResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(), null));
            apiResponses.addApiResponse("403", OpenApiUtils.createApiResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), null));
        }
        if (apiErrorBase != null) {
            apiResponses.addApiResponse("404", OpenApiUtils.createApiResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), apiErrorBase));
        }
        if (apiErrorTrace != null) {
            apiResponses.addApiResponse("500", OpenApiUtils.createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), apiErrorTrace));
        }
        apiResponses.addApiResponse("503", OpenApiUtils.createApiResponse(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), null));
    }

    private static final MediaType getAllMediaType(Content content) {
        return content != null ? content.get(org.springframework.http.MediaType.ALL_VALUE) : null;
    }

    private static final void setAllMediaType(Operation operation) {
        if (operation == null) {
            return;
        }
        setAllMediaType(getContentDefaultResponse(operation));
    }

    private static final void setAllMediaType(Content content) {
        MediaType mediaType = getAllMediaType(content);
        if (mediaType == null || mediaType.getSchema() == null) {
            return;
        }
        if (mediaType.getSchema().getType() != null) {
            content.put(org.springframework.http.MediaType.TEXT_PLAIN_VALUE, mediaType);
            content.remove(org.springframework.http.MediaType.ALL_VALUE);
        } else if (mediaType.getSchema().get$ref() != null) {
            content.put(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
            content.remove(org.springframework.http.MediaType.ALL_VALUE);
        }
    }

    private static final Content getContentDefaultResponse(Operation operation) {
        ApiResponse response = getDefaultResponse(operation);
        return response != null ? response.getContent() : null;
    }

    private static final ApiResponse getDefaultResponse(Operation operation) {
        if (operation == null || operation.getResponses() == null) {
            return null;
        }
        ApiResponse response = operation.getResponses().get("200");
        if (response != null) {
            return response;
        }
        return operation.getResponses().get("201");
    }

}
