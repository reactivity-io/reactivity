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


package io.reactivity.core.broadcaster.repository.couchbase;

import com.couchbase.client.java.Bucket;
import io.reactivity.core.lib.ReactivityEntity;
import io.reactivity.core.lib.ViewType;
import io.reactivity.core.lib.event.ArtifactView;
import io.reactivity.core.lib.event.Member;
import io.reactivity.core.lib.event.Organization;
import io.reactivity.core.lib.event.Period;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;
import rx.Observable;
import rx.RxReactiveStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Function;

/**
 * <p>
 * A repository providing mocked data for missing methods implementation in {@link CouchbaseReactvityRepository}.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@Repository
public class MockCouchbaseReactivityRepository extends CouchbaseReactvityRepository {

    /**
     * Resource loader.
     */
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * <p>
     * Builds a new repository.
     * </p>
     *
     * @param bucket the bucket
     * @throws IOException if the repository is not able to initialize the views
     */
    @Autowired
    public MockCouchbaseReactivityRepository(final Bucket bucket) throws IOException {
        super(bucket);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Publisher<T> findViewById(final String id, final Function<ReactivityEntity, T> mapper) {
        return findViewsFromOrganization("Organization/1", mapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Publisher<T> findViewsFromOrganization(final String organizationId, final Function<ReactivityEntity, T> mapper) {
        return RxReactiveStreams.toPublisher(
                Observable.fromCallable(() -> new ArtifactView(
                        "0.1.0-SNAPSHOT",
                        nextMockedId(),
                        nexMockTimestamp(),
                        organizationId,
                        "Last artifacts created in your organization",
                        new Period(null, null, 100, null),
                        ViewType.LIST.name()))
                        .map(mapper::apply));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Publisher<T> findOrganizationsWithMember(final String memberId, final Function<ReactivityEntity, T> mapper) {
        return RxReactiveStreams.toPublisher(Observable.fromCallable(() -> new Organization(
                "0.1.0-SNAPSHOT",
                "Organization/1",
                nexMockTimestamp(),
                "Reactivity",
                loadMockedImage("react-3.png"),
                Collections.singletonList(new Member("User/1", "ADMIN")))).map(mapper::apply));
    }

    private String nextMockedId() {
        return UUID.randomUUID().toString();
    }

    private long nexMockTimestamp() {
        return System.currentTimeMillis();
    }

    private String loadMockedImage(final String mockName) {
        final Resource cpr = resourceLoader.getResource("classpath:mocks/" + mockName);
        final byte[] picture;

        try (final InputStream is = cpr.getInputStream()) {
            picture = FileCopyUtils.copyToByteArray(is);
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }

        return Base64.getEncoder().encodeToString(picture);
    }
}
