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


package io.reactivity.core.broadcaster.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <p>
 * This filter creates resolves a session a refer it in the server exchange attributes.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class SessionFilter implements WebFilter {

    /**
     * Session repository.
     */
    private final SessionRepository<ExpiringSession> sessionRepository;

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param sessionRepository the session repository
     */
    public SessionFilter(final SessionRepository<ExpiringSession> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final HttpCookie cookie = exchange.getRequest().getCookies().getFirst("SESSION");

        if (cookie != null) {
            ExpiringSession session = sessionRepository.getSession(cookie.getValue());

            if (session == null) {
                session = new MapSession(cookie.getValue());
                sessionRepository.save(session);
            } else {
                if (session.isExpired()) {
                    logger.info("Session {} has expired, auto-refreshing...", session.getId());
                }

                session.setLastAccessedTime(System.currentTimeMillis());
            }

            exchange.getAttributes().put(ExpiringSession.class.getName(), session);
        }

        return chain.filter(exchange);
    }
}
