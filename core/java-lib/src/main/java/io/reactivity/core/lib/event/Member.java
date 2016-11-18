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

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <p>
 * Representation of an organization member. The details about the user can be found with an ID and a role specifies
 * the rights of this user inside the organization.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {

    /**
     * The user ID.
     */
    private String id;

    /**
     * The user role within the organization.
     */
    private String role;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param id the user ID
     * @param role the user role
     */
    public Member(final String id, final String role) {
        this.id = id;
        this.role = role;
    }

    /**
     * <p>
     * Gets the ID.
     * </p>
     *
     * @return the user ID
     */
    public String getId() {
        return id;
    }

    /**
     * <p>
     * Gets the role.
     * </p>
     *
     * @return the user role
     */
    public String getRole() {
        return role;
    }
}
