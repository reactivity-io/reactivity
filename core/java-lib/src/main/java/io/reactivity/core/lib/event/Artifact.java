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

import java.util.List;
import java.util.Map;

/**
 * <p>
 * A representation of an artifact that is streamed across the Reactivity platform.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class Artifact extends ReactivityEntity {

    /**
     * A list of views containing this artifact.
     */
    private final List<String> views;

    /**
     * All categories defined in that artifact.
     */
    private final Map<String, Object> categories;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param version the entity version
     * @param id the artifact ID
     * @param timestamp when the artifact was created
     * @param views the views containing the artifact
     * @param categories the categories
     */
    public Artifact(final Version version,
                    final String id,
                    final long timestamp,
                    final List<String> views,
                    final Map<String, Object> categories) {
        super(version, id, timestamp);

        this.views = views;
        this.categories = categories;
    }

    /**
     * <p>
     * Gets the views.
     * </p>
     *
     * @return the artifact views
     */
    public List<String> getViews() {
        return views;
    }

    /**
     * <p>
     * Gets the categories.
     * </p>
     *
     * @return the artifact categories
     */
    public Map<String, Object> getCategories() {
        return categories;
    }
}
