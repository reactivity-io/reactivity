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


package io.reactivity.core.broadcaster.repository;

import io.reactivity.core.lib.ReactivityEntity;
import io.reactivity.core.lib.event.ArtifactView;
import org.reactivestreams.Publisher;

import java.util.function.Function;

/**
 * <p>
 * Data repository for Reactivity. Fundamental objects are retrieved through this repository.
 * All methods return a {@code Publisher} that sends data are available after a query execution.
 * The publisher type is specified by a {@code Function} provided through the parameters that acts as a mapper from
 * the supplied particular {@link io.reactivity.core.lib.ReactivityEntity}.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public interface ReactivityRepository {

    /**
     * <p>
     * Gets all the organizations associated to the given member.
     * </p>
     *
     * @param memberId the member ID
     * @param mapper the map function to the desired returned type
     * @param <T> the desired returned type
     * @return a publisher where source is the retrieved organizations
     */
    <T> Publisher<T> findOrganizationsWithMember(String memberId, Function<ReactivityEntity, T> mapper);

    /**
     * <p>
     * Gets all the views associated to the given organization.
     * </p>
     *
     * @param organizationId the organization ID
     * @param mapper the map function to the desired returned type
     * @param <T> the desired returned type
     * @return a publisher where source is the retrieved views
     */
    <T> Publisher<T> findViewsFromOrganization(String organizationId, Function<ReactivityEntity, T> mapper);

    /**
     * <p>
     * Gets all the artifacts associated to the given view.
     * </p>
     *
     * @param view the view
     * @param mapper the map function to the desired returned type
     * @param <T> the desired returned type
     * @return a publisher where source is the retrieved artifacts
     */
    <T> Publisher<T> findArtifactFromView(ArtifactView view, Function<ReactivityEntity, T> mapper);

    /**
     * <p>
     * Finds a view with a particular ID.
     * </p>
     *
     * @param id the view ID
     * @param mapper the map function to the desired returned type
     * @param <T> the desired returned type
     * @return a publisher where source is the retrieved view
     */
    <T> Publisher<T> findViewById(String id, Function<ReactivityEntity, T> mapper);
}
