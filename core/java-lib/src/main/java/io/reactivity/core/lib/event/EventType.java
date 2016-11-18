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


package io.reactivity.core.lib.event;

import io.reactivity.core.lib.ReactivityEntity;

/**
 * <p>
 * Enumeration of all supported core event types in the {@link Event#event} field.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public enum EventType {

    /**
     * Read artifact event.
     */
    READ_ARTIFACT,

    /**
     * Read view event.
     */
    READ_VIEW,

    /**
     * Read organization event.
     */
    READ_ORGANIZATION;

    /**
     * <p>
     * Builds a new event based on a free event type and a data payload.
     * </p>
     *
     * @param eventType the event type
     * @param data the data
     * @param <T> the payload type that must be a {@link ReactivityEntity}
     * @return the event
     */
    public static <T extends ReactivityEntity> Event<T> newEvent(final String eventType, final T data) {
        return new Event<>(eventType, data);
    }

    /**
     * <p>
     * Creates a new {@link Event} with a type corresponding to this enumeration.
     * </p>
     *
     * @param data the data payload
     * @param <T> the payload type that must be a {@link ReactivityEntity}
     * @return the event
     */
    public <T extends ReactivityEntity> Event<T> newEvent(final T data) {
        return newEvent(name(), data);
    }
}
