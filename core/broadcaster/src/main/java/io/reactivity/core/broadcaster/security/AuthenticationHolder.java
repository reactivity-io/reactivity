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

import java.util.function.Supplier;

/**
 * <p>
 * This class holds in a {@code ThreadLocal} a {@code Supplier} of {@code Authentication}.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class AuthenticationHolder {

    /**
     * The {@code ThreadLocal}.
     */
    private static final ThreadLocal<Supplier<Authentication>> CURRENT_AUTHENTICATION = new ThreadLocal<>();

    /**
     * <p>
     * Gets the {@code Authentication} object.
     * </p>
     *
     * @return the authentication
     */
    static Authentication getCurrentAuthentication() {
        return CURRENT_AUTHENTICATION.get().get();
    }

    /**
     * <p>
     * Sets the {@code Supplier} of {@code Authentication}.
     * </p>
     *
     * @param supplier the supplier
     */
    static void setAuthenticationSupplier(final Supplier<Authentication> supplier) {
        CURRENT_AUTHENTICATION.set(supplier);
    }
}
