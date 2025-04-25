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

package org.flcit.springboot.web.openapi.configuration.simple.oauth2;

import java.util.Objects;

import org.springdoc.core.customizers.OpenApiCustomiser;

import org.flcit.springboot.web.openapi.configuration.simple.BaseOpenApiSimpleConfiguration;
import org.flcit.springboot.web.openapi.customiser.Oauth2ClientCredentialsOpenApiCustomiser;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class OpenApiOauth2SimpleConfiguration extends BaseOpenApiSimpleConfiguration {

    private String tokenUrl;

    /**
     * 
     */
    public OpenApiOauth2SimpleConfiguration() { }

    /**
     * @param onlyDocs
     */
    public OpenApiOauth2SimpleConfiguration(boolean onlyDocs) {
        setOnlyDocs(onlyDocs);
    }

    /**
     * @param tokenUrl
     */
    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    /**
     *
     */
    @Override
    public OpenApiCustomiser getAuthenficationCustomiser(String[] groupes) {
        return new Oauth2ClientCredentialsOpenApiCustomiser(tokenUrl, groupes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(tokenUrl);
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
        OpenApiOauth2SimpleConfiguration other = (OpenApiOauth2SimpleConfiguration) obj;
        return Objects.equals(tokenUrl, other.tokenUrl);
    }

}
