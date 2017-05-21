/*
 * The MIT License (MIT) Copyright (c) 2016 The reactivity authors
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


package io.reactivity.core.broadcaster.service;

import io.reactivity.core.lib.ReactivityEntity;
import io.reactivity.core.lib.event.Error;
import io.reactivity.core.lib.event.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import rsc.publisher.PublisherJust;

import java.time.Duration;

/**
 * <p>
 * This aspect modifies the {@code Flux} returned by {@link EventService} methods in order to add a timeout and an error
 * handlers in a generic way.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@Aspect
@Component
public class FluxDecorator {

    /**
     * Timeout applied to {@code Flux}.
     */
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * <p>
     * Method interceptor that decorates the returned {@code Flux} with a {@link #TIMEOUT timeout} handler,
     * an {@link Error error} handler and a log name corresponding to a {@code String} representation of the
     * method invocation.
     * </p>
     *
     * @param proceedingJoinPoint the join point
     * @return the modified {@code Flux}
     * @throws Throwable if proceed fails
     */
    @Around("execution(* io.reactivity.core.broadcaster.service.EventService.*(..))")
    @SuppressWarnings("unchecked")
    public Flux<Event<? extends ReactivityEntity>> decorate(final ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        return decorate(Flux.class.<Event<? extends ReactivityEntity>>cast(proceedingJoinPoint.proceed()),
                proceedingJoinPoint.toLongString());
    }

    /**
     * <p>
     * Decorates the returned {@code Flux} with a {@link #TIMEOUT timeout} handler, an {@link Error error} handler and a
     * log name.
     * </p>
     *
     * @param flux the {@code Flux} to decorate
     * @param log the log name
     * @return the decorated {@code Flux}
     */
    Flux<Event<ReactivityEntity>> decorate(final Flux<Event<ReactivityEntity>> flux, final String log) {
        return flux.timeout(TIMEOUT, PublisherJust.fromCallable(Error::timeoutEvent))
                .onErrorResume(err -> {
                    logger.error("Intercepted Flux error", err);
                    return PublisherJust.fromCallable(Error::exceptionEvent);
                }).log(log);
    }
}
