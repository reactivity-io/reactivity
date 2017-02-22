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
import io.reactivity.core.lib.Version;

/**
 * <p>
 * A representation of a view allowing to query a set of artifacts from the associated organization.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class ArtifactView extends ReactivityEntity {

    /**
     * The organization ID.
     */
    private final String organization;

    /**
     * The view name.
     */
    private final String name;

    /**
     * The period that covers the artifacts.
     */
    private final Period period;

    /**
     * The type of view.
     */
    private final String type;

    /**
     * <p>
     * Builds a new instance by copy and a specific period.
     * </p>
     *
     * @param other the view to copy
     * @param period the period covering the selected artifacts
     */
    public ArtifactView(final ArtifactView other,
                        final Period period) {
        super(other.getVersion(), other.getId(), other.getUpdated());

        this.organization = other.getOrganization();
        this.name = other.getType();
        this.period = period;
        this.type = other.getType();
    }

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param version the entity version
     * @param id the view ID
     * @param timestamp when the view was created
     * @param organization the organization ID
     * @param name the view name
     * @param period the period covering the selected artifacts
     * @param type the view type
     */
    public ArtifactView(final Version version,
                        final String id,
                        final long timestamp,
                        final String organization,
                        final String name,
                        final Period period,
                        final String type) {
        super(version, id, timestamp);

        this.organization = organization;
        this.name = name;
        this.period = period;
        this.type = type;
    }

    /**
     * <p>
     * Gets the organization ID.
     * </p>
     *
     * @return the view's organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * <p>
     * Gets the name.
     * </p>
     *
     * @return the view's name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Gets the period.
     * </p>
     *
     * @return the view's period
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * <p>
     * Gets the type.
     * </p>
     *
     * @return the view type
     */
    public String getType() {
        return type;
    }
}
