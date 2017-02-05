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

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * Represents an authentication state in reactivity.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class ReactivityAuthentication extends AbstractAuthenticationToken {

    /**
     * The authenticated user details.
     */
    private final UserDetails userDetails;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param userDetails the user details
     */
    public ReactivityAuthentication(final UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCredentials() {
        return userDetails.getPassword();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
