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

package org.flcit.springboot.web.openapi.customiser;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.util.ObjectUtils;

import org.flcit.commons.core.util.StringUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * @deprecated Basic Authentification mode will be removed in a future version
 */
/**
 * 
 * @since 
 * @author Florian Lestic
 */
@Deprecated
public class BasicAuthentificationOpenApiCustomiser implements OpenApiCustomiser {

    private final String[] groupes;

    /**
     * @param groupes
     */
    public BasicAuthentificationOpenApiCustomiser(String... groupes) {
        this.groupes = groupes;
    }

    /**
     *
     */
    @Override
    public void customise(OpenAPI openApi) {
        openApi.addSecurityItem(new SecurityRequirement().addList("Authorization"))
        .getComponents()
        .addSecuritySchemes("Authorization", new SecurityScheme()
                .description(String.format("Compte de service LDAP : %s", ObjectUtils.isEmpty(groupes) ? StringUtils.EMPTY : String.join(", ", groupes)))
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic"));
    }

}
