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

import java.util.Arrays;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.ObjectUtils;

import org.flcit.commons.core.util.StringUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class Oauth2ClientCredentialsOpenApiCustomiser implements OpenApiCustomiser {

    private final String tokenUrl;
    private final String[] roles;

    /**
     * @param tokenUrl
     * @param authorities
     */
    public Oauth2ClientCredentialsOpenApiCustomiser(String tokenUrl, GrantedAuthority... authorities) {
        this(tokenUrl,
                Arrays.stream(authorities)
                .map(Oauth2ClientCredentialsOpenApiCustomiser::getAuthority)
                .toArray(String[]::new)
        );
    }

    private static final String getAuthority(GrantedAuthority grantedAuthority) {
        return grantedAuthority.getAuthority();
    }

    /**
     * @param tokenUrl
     * @param roles
     */
    public Oauth2ClientCredentialsOpenApiCustomiser(String tokenUrl, String... roles) {
        this.tokenUrl = tokenUrl;
        this.roles = roles;
    }

    /**
     *
     */
    @Override
    public void customise(OpenAPI openApi) {
        openApi.addSecurityItem(new SecurityRequirement().addList("Oauth2"))
        .getComponents()
        .addSecuritySchemes("Oauth2", new SecurityScheme()
                .description(String.format("Rôle(s) : %s", ObjectUtils.isEmpty(roles) ? StringUtils.EMPTY : String.join(", ", roles)))
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .clientCredentials(new OAuthFlow()
                                .scopes(new Scopes().addString("openid", StringUtils.EMPTY))
                                .tokenUrl(tokenUrl))));
    }

}
