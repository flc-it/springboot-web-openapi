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

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class OpenApiEndpoints {

    private OpenApiEndpoints() { }

    /**
     * @return
     */
    public static RequestMatcher[] toApiDocsEndpoint() {
        return new RequestMatcher[] {
                new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.toString())
        };
    }

    /**
     * @return
     */
    public static RequestMatcher[] toAnyEndpoints() {
        return new RequestMatcher[] {
                new AntPathRequestMatcher("/swagger-ui.html", HttpMethod.GET.toString()),
                new AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.toString()),
                new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.toString())
        };
    }

}
