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

package org.flcit.springboot.web.openapi.configuration.simple;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;

import org.flcit.springboot.web.openapi.configuration.BaseOpenApiConfiguration;
import org.flcit.springboot.web.openapi.util.OpenApiUtils;
import io.swagger.v3.oas.models.Operation;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public abstract class BaseOpenApiSimpleConfiguration extends BaseOpenApiConfiguration {

    private String[] userGroupes;
    private String[] adminGroupes;
    private boolean onlyDocs;

    /**
     * @param userGroupes
     */
    public void setUserGroupes(String[] userGroupes) {
        this.userGroupes = userGroupes;
    }

    /**
     * @param adminGroupes
     */
    public void setAdminGroupes(String[] adminGroupes) {
        this.adminGroupes = adminGroupes;
    }

    /**
     *
     */
    @Override
    public boolean isOnlyDocs() {
        return onlyDocs;
    }

    /**
     * @param onlyDocs
     */
    public void setOnlyDocs(boolean onlyDocs) {
        this.onlyDocs = onlyDocs;
    }

    /**
     * @return
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("1-API-User")
                .pathsToMatch("/api/**")
                .addOpenApiCustomiser(OpenApiUtils.openApiCustomiser(getAuthenficationCustomiser(userGroupes), globalResponses(true, getCustomUserConsumersOperation())))
                .build();
    }

    /**
     * @return
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("2-API-Admin")
                .pathsToMatch("/actuator/*")
                .addOpenApiCustomiser(OpenApiUtils.openApiCustomiser(getAuthenficationCustomiser(adminGroupes), globalResponses(true, getCustomAdminConsumersOperation())))
                .build();
    }

    /**
     * @param groupes
     * @return
     */
    public abstract OpenApiCustomiser getAuthenficationCustomiser(String[] groupes);

    /**
     * @return
     */
    @SuppressWarnings("java:S1168")
    public Consumer<Operation>[] getCustomUserConsumersOperation() {
        return null;
    }

    /**
     * @return
     */
    @SuppressWarnings("java:S1168")
    public Consumer<Operation>[] getCustomAdminConsumersOperation() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(adminGroupes);
        result = prime * result + Arrays.hashCode(userGroupes);
        result = prime * result + Objects.hash(onlyDocs);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseOpenApiSimpleConfiguration other = (BaseOpenApiSimpleConfiguration) obj;
        return Arrays.equals(adminGroupes, other.adminGroupes) && onlyDocs == other.onlyDocs
                && Arrays.equals(userGroupes, other.userGroupes);
    }

}
