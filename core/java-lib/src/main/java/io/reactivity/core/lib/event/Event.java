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
 * Representation of a reactivity event. An event is identified by the ID of its data (which must be a
 * {@link io.reactivity.core.lib.ReactivityEntity}), and the event type.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 * @param <T>
 */
public class Event<T extends ReactivityEntity> {

    /**
     * The version.
     */
    private final String version;

    /**
     * The event ID.
     */
    private final String id;

    /**
     * The event type.
     */
    private final String event;

    /**
     * When the data was created/updated.
     */
    private final long updated;

    /**
     * The data payload.
     */
    private final T data;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param event the event type
     * @param data the data payload
     */
    public Event(final String event, final T data) {
        this.id = data.getId();
        this.event = event;
        this.updated = data.getUpdated();
        this.data = data;
        this.version = data.getVersion();
    }

    /**
     * <p>
     * Gets the version.
     * </p>
     *
     * @return the data version
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * Gets the ID.
     * </p>
     *
     * @return the data ID
     */
    public String getId() {
        return id;
    }

    /**
     * <p>
     * Gets the event type.
     * </p>
     *
     * @return the event type
     */
    public String getEvent() {
        return event;
    }

    /**
     * <p>
     * Gets the creation or last updated date.
     * </p>
     *
     * @return the data timestamp
     */
    public long getUpdated() {
        return updated;
    }

    /**
     * <p>
     * Gets the data.
     * </p>
     *
     * @return the payload
     */
    public T getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("event type: %s / id: %s", event, id);
    }
}
