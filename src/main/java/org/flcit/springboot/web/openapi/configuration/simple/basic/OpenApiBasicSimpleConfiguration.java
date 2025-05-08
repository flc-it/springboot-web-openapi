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

package org.flcit.springboot.web.openapi.configuration.simple.basic;

import org.springdoc.core.customizers.OpenApiCustomiser;

import org.flcit.springboot.web.openapi.configuration.simple.BaseOpenApiSimpleConfiguration;
import org.flcit.springboot.web.openapi.customiser.BasicAuthentificationOpenApiCustomiser;

/**
 * @deprecated Basic Authentification mode will be removed in a future version
 */
/**
 * 
 * @since 
 * @author Florian Lestic
 */
@Deprecated
public class OpenApiBasicSimpleConfiguration extends BaseOpenApiSimpleConfiguration {

    /**
     * 
     */
    public OpenApiBasicSimpleConfiguration() { }

    /**
     * @param onlyDocs
     */
    public OpenApiBasicSimpleConfiguration(boolean onlyDocs) {
        setOnlyDocs(onlyDocs);
    }

    /**
     *
     */
    @Override
    public OpenApiCustomiser getAuthenficationCustomiser(String[] groupes) {
        return new BasicAuthentificationOpenApiCustomiser(groupes);
    }

}
