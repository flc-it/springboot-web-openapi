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

package org.flcit.springboot.web.openapi;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import org.flcit.springboot.commons.core.condition.ConditionalOnEnvironment;
import org.flcit.springboot.commons.core.util.EnvironmentUtils;
import org.flcit.springboot.web.openapi.configuration.BaseOpenApiConfiguration;
import org.flcit.springboot.web.openapi.configuration.simple.basic.OpenApiBasicSimpleConfiguration;
import org.flcit.springboot.web.openapi.configuration.simple.oauth2.OpenApiOauth2SimpleConfiguration;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
@SuppressWarnings("deprecation")
@AutoConfiguration
public class OpenApiAutoConfiguration implements EnvironmentPostProcessor {

    @Configuration
    @Profile("doc")
    @ConditionalOnMissingBean(BaseOpenApiConfiguration.class)
    @ConditionalOnProperty("openapi.simple-docs")
    @ConditionalOnEnvironment(value = "prd", notIn = true)
    static class BaseOpenApiSimpleDocsConfiguration {
        /**
         * @deprecated Basic Authentification mode will be removed in a future version
         */
        @Deprecated
        @Bean
        @ConditionalOnProperty("openapi.simple-docs.basic")
        @ConfigurationProperties("openapi")
        public BaseOpenApiConfiguration getOpenApiBasicSimpleDocsConfiguration() {
            return new OpenApiBasicSimpleConfiguration(true);
        }
        @Bean
        @ConditionalOnProperty(value = "openapi.simple-docs.basic", havingValue = "false", matchIfMissing = true)
        @ConfigurationProperties("openapi")
        public BaseOpenApiConfiguration getOpenApiSimpleDocsConfiguration() {
            return new OpenApiOauth2SimpleConfiguration(true);
        }
    }

    @Configuration
    @ConditionalOnMissingBean(BaseOpenApiConfiguration.class)
    @ConditionalOnProperty("openapi.simple-online")
    @ConditionalOnEnvironment(value = "prd", notIn = true)
    static class BaseOpenApiSimpleOnlineConfiguration {
        /**
         * @deprecated Basic Authentification mode will be removed in a future version
         */
        @Deprecated
        @Bean
        @ConditionalOnProperty("openapi.simple-online.basic")
        @ConfigurationProperties("openapi")
        public BaseOpenApiConfiguration getOpenApiBasicSimpleOnlineConfiguration() {
            return new OpenApiBasicSimpleConfiguration();
        }
        @Bean
        @ConditionalOnProperty(value = "openapi.simple-online.basic", havingValue = "false", matchIfMissing = true)
        @ConfigurationProperties("openapi")
        public BaseOpenApiConfiguration getOpenApiSimpleOnlineConfiguration() {
            return new OpenApiOauth2SimpleConfiguration();
        }
    }

    /**
     *
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (EnvironmentUtils.isEnvironmentPrd(environment)) {
            try {
                environment.getPropertySources().addLast(new ResourcePropertySource("classpath:web-openapi-lib.properties"));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
