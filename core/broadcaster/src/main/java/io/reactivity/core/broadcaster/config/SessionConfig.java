/*
 * The MIT License (MIT) Copyright (c) 2017 The reactivity authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package io.reactivity.core.broadcaster.config;

import io.reactivity.core.broadcaster.session.SessionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.web.server.WebFilter;

/**
 * <p>
 * Configures session support for Reactivity.
 * </p>
 *
 * <p>
 * The session infrastructure of reactivity is based on a mix of spring-session, which is feature complete for servlet
 * containers only, and the spring web API which abstracts the servlet API. In that way, a modest but effective session
 * support can be leveraged on non-servlet containers keeping in the same time the opportunity to bridge session
 * management to spring-session advanced features.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@Configuration
public class SessionConfig {

    /**
     * <p>
     * Creates a {@code WebFilter} that populates the {@code SessionRepository} with an {@code ExpiringSession} object.
     * </p>
     *
     * <p>
     * This filter has a zero {@code @Order} to be executed first in the filter chain.
     * </p>
     *
     * @return the filter
     */
    @Bean
    @Order(0)
    WebFilter webFilter() {
        return new SessionFilter(sessionRepository());
    }

    /**
     * <p>
     * Creates a {@code SessionRepository} that will be used by the {@link SessionFilter} as session store.
     * The implementation can be delegated to one of the various classes in spring-session project.
     * </p>
     *
     * @return the spring session repository
     */
    @Bean
    SessionRepository<ExpiringSession> sessionRepository() {
        return new MapSessionRepository();
    }
}
