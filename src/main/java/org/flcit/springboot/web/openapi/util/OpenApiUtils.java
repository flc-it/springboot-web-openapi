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

package org.flcit.springboot.web.openapi.util;

import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomiser;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class OpenApiUtils {

    private OpenApiUtils() { }

    @SuppressWarnings("rawtypes")
    private static final void addSchemas(OpenAPI openApi, Map<String, Schema> schemas) {
        if (schemas == null || schemas.isEmpty()) {
            return;
        }
        if (openApi.getComponents().getSchemas() != null) {
            openApi.getComponents().getSchemas().putAll(schemas);
        } else {
            openApi.getComponents().setSchemas(new HashMap<>(schemas));
        }
    }

    @SuppressWarnings("rawtypes")
    private static final Schema getRefSchema(Class<?> clazz) {
        Schema schema = new Schema();
        schema.setName(clazz.getSimpleName());
        schema.set$ref("#/components/schemas/" + clazz.getSimpleName());
        return schema;
    }

    @SuppressWarnings("rawtypes")
    private static final Schema getSchema(OpenAPI openApi, Class<?> clazz) {
        if (openApi.getComponents().getSchemas() != null && openApi.getComponents().getSchemas().containsKey(clazz.getSimpleName())) {
            return getRefSchema(clazz);
        }
        return null;
    }

    /**
     * @param openApi
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static final Schema getOrAddSchemas(OpenAPI openApi, Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        final Schema schema = getSchema(openApi, clazz);
        return schema != null ? schema : addSchemas(openApi, clazz);
    }

    @SuppressWarnings("rawtypes")
    private static final Schema addSchemas(OpenAPI openApi, Class<?> clazz) {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(clazz);
        addSchemas(openApi, schemas);
        return getRefSchema(clazz);
    }

    /**
     * @param description
     * @param schema
     * @return
     */
    public static final ApiResponse createApiResponse(String description, Schema<?> schema) {
        return createApiResponse(description, schema, org.springframework.http.MediaType.APPLICATION_JSON);
    }

    private static final ApiResponse createApiResponse(String description, Schema<?> schema, org.springframework.http.MediaType mediaType) {
        if (schema != null) {
            MediaType mediaTypeIntern = new MediaType();
            mediaTypeIntern.schema(schema);
            return new ApiResponse().description(description)
                    .content(new Content().addMediaType(mediaType.toString(), mediaTypeIntern));
        } else {
            return new ApiResponse().description(description);
        }
    }

    /**
     * @param openApiCustomisers
     * @return
     */
    public static final OpenApiCustomiser openApiCustomiser(OpenApiCustomiser... openApiCustomisers) {
        return openApi -> {
            for (OpenApiCustomiser openApiCustomiser: openApiCustomisers) {
                openApiCustomiser.customise(openApi);
            }
        };
    }

}
