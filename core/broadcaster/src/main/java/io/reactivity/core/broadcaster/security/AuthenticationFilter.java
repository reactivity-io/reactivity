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


package io.reactivity.core.broadcaster.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.session.ExpiringSession;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * <p>
 * This filter authenticates the user request according to the current session in the HTTP request.
 * An {@code Authentication} object will be provided through the {@link AuthenticationHolder}.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class AuthenticationFilter implements WebFilter {

    /**
     * The service providing user details.
     */
    private final UserDetailsService userDetailsService;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param userDetailsService the user details service
     */
    public AuthenticationFilter(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final Optional<ExpiringSession> optional = exchange.getAttribute(ExpiringSession.class.getName());

        optional.ifPresent(session -> {
            UserDetails userDetails = session.getAttribute(UserDetails.class.getName());

            if (userDetails == null) {
                userDetails = userDetailsService.loadUserByUsername("anonymous");
                session.setAttribute(UserDetails.class.getName(), userDetails);
            }

            AuthenticationHolder.setAuthenticationSupplier(new AuthenticationSupplier(userDetails, session::isExpired));
        });

        return chain.filter(exchange);
    }

    /**
     * <p>
     * A {@code Supplier} that provides {@link Authentication}.
     * </p>
     *
     * @author Guillaume DROUET
     * @version 0.1.0
     */
    private static class AuthenticationSupplier implements Supplier<Authentication> {

        /**
         * Authentication object lazily instantiated.
         */
        private Authentication authentication;

        /**
         * The user details.
         */
        private UserDetails userDetails;

        /**
         * If the session has expired.
         */
        private Supplier<Boolean> isExpired;

        /**
         * <p>
         * Builds a new instance.
         * </p>
         *
         * @param userDetails the user details
         * @param isExpired provides a {@code boolean} telling if the associated session has expired or not
         */
        AuthenticationSupplier(final UserDetails userDetails, final Supplier<Boolean> isExpired) {
            this.userDetails = userDetails;
            this.isExpired = isExpired;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Authentication get() {
            if (authentication == null) {
                authentication = new ReactivityAuthentication(userDetails);
                authentication.setAuthenticated(!isExpired.get());
            }

            return authentication;
        }
    }
}