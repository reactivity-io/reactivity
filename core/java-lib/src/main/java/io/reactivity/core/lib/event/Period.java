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
 * A period of time defined by a {@link ArtifactView}. Artifacts can be selected by defining a range of timestamps.
 * One of the two timestamps (the first corresponding to the oldest artifact and the second to the newest artifact)
 * can be {@code null} or negative to be ignored. A limit of returned artifact can be specified.
 * Otherwise, artifacts are selected without limit.
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Period {

    /**
     * The artifacts are selected from this timestamp.
     */
    private Long from;

    /**
     * The artifacts are selected until this timestamp.
     */
    private Long to;

    /**
     * A limit of returned artifact can be specified.
     */
    private Integer limit;

    /**
     * A particular category that will be compared to the timestamp instead of the last update date.
     */
    private String category;

    /**
     * <p>
     * Builds a new instance with a bounded period.
     * </p>
     *
     * @param from starting timestamp
     * @param to ending timestamp
     * @param limit the maximum number of returned artifacts
     * @param category the timestamp category
     */
    public Period(final Long from, final Long to, final Integer limit, final String category) {
        this.from = from;
        this.to = to;
        this.limit = limit;
        this.category = category;
    }

    /**
     * <p>
     * Gets the {@code from} timestamp.
     * </p>
     *
     * @return the when the artifacts start
     */
    public Long getFrom() {
        return from;
    }

    /**
     * <p>
     * Gets the {@code to} timestamp.
     * </p>
     *
     * @return the when the artifacts end
     */
    public Long getTo() {
        return to;
    }

    /**
     * <p>
     * Gets the maximum number artifacts.
     * </p>
     *
     * @return the maximum number of artifacts
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * <p>
     * Gets the from timestamp category.
     * </p>
     *
     * @return the category to compare with timestamps
     */
    public String getCategory() {
        return category;
    }
}
