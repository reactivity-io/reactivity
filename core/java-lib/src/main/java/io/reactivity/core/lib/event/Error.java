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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * <p>
 * An error representation.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class Error extends ReactivityEntity {

    /**
     * Application version.
     */
    private static String version;

    /**
     * Error message.
     */
    private final String message;

    // Loads lazily the properties
    static {
        final Properties properties;

        try (final InputStream is = Error.class.getResourceAsStream("/io/reactivity/core/lib/application.properties")) {
            properties = new Properties();
            properties.load(is);
            version = properties.getProperty("application.version");
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    /**
     * <p>
     * Builds a new instance
     * </p>
     *
     * @param message the error message
     */
    public Error(final String message) {
        super(version, UUID.randomUUID().toString(), System.currentTimeMillis());
        this.message = message;
    }

    /**
     * <p>
     * Creates a timeout error.
     * </p>
     *
     * @return the error
     */
    public static Event<Error> timeout() {
        return EventType.ERROR.newEvent(new Error("The service has timed out."));
    }

    /**
     * <p>
     * Creates a exception error.
     * </p>
     *
     * @return the error
     */
    public static Event<Error> exception() {
        return EventType.ERROR.newEvent(new Error("An error has occurred. See logs for more details."));
    }

    /**
     * <p>
     * Gets the error message.
     * </p>
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
