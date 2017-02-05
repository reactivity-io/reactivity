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

import io.reactivity.core.broadcaster.security.AuthenticationFilter;
import io.reactivity.core.broadcaster.security.ReactivityAuthenticationObjectFactory;
import io.reactivity.core.broadcaster.session.ReactivitySessionScope;
import io.reactivity.core.broadcaster.session.ReactivityUserDetailsService;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.WebFilter;

/**
 * <p>
 * Configures security support for Reactivity.
 * </p>
 *
 * <p>
 * The security infrastructure of reactivity is based on a mix of spring-security, which is feature complete for servlet
 * containers only, and the spring web API which abstracts the servlet API. In that way, a modest but effective security
 * support can be leveraged on non-servlet containers keeping in the same time the opportunity to bridge security
 * management to spring-security advanced features.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@Configuration
public class SecurityConfig {

    /**
     * <p>
     * Creates a {@code Scope} dedicated to reactivity session scope.
     * </p>
     *
     * @return the scope
     */
    Scope reactivitySessionScope() {
        return new ReactivitySessionScope();
    }

    /**
     * <p>
     * Configures a custom {@code Scope} called {@code REACTIVITY_SESSION} that allows
     * been to be autowired with an {@code Authentication} object resolved from the session.
     * </p>
     *
     * @param clbf the custom object factory registry
     * @return the custom scope registry
     */
    @Bean
    CustomScopeConfigurer customScopeConfigurer(final ConfigurableListableBeanFactory clbf) {
        final CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        customScopeConfigurer.addScope(ReactivitySessionScope.REACTIVITY_SESSION, reactivitySessionScope());
        clbf.registerResolvableDependency(Authentication.class, new ReactivityAuthenticationObjectFactory());
        return customScopeConfigurer;
    }

    /**
     * <p>
     * Creates a {@code WebFilter} that populates the
     * {@link io.reactivity.core.broadcaster.security.AuthenticationHolder}
     * with an {@code Authentication} object resolved from a session populated
     * by the {@link io.reactivity.core.broadcaster.session.SessionFilter}.
     * </p>
     *
     * @return the filter
     */
    @Bean
    WebFilter authenticationFilter() {
        return new AuthenticationFilter(userDetailsService());
    }

    /**
     * <p>
     * Returns a {@code UserDetailsService} that loads user information to be stored in the {@code Authentication}.
     * </p>
     *
     * @return the user details service
     */
    @Bean
    UserDetailsService userDetailsService() {
        return new ReactivityUserDetailsService();
    }

}
