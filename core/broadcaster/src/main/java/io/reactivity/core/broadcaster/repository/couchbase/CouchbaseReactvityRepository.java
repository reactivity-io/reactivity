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

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import io.reactivity.core.broadcaster.repository.ReactivityRepository;
import io.reactivity.core.lib.event.Artifact;
import io.reactivity.core.lib.event.ArtifactView;
import org.reactivestreams.Publisher;
import rx.Observable;
import rx.RxReactiveStreams;

import java.io.IOException;

import java.util.function.Function;

/**
 * <p>
 * This repository performs operations on {@link Artifact} documents with {@code RxJava} support.
 * </p>
 */
abstract class CouchbaseReactvityRepository implements ReactivityRepository {

    /**
     * The bucket.
     */
    private final AsyncBucket bucket;

    /**
     * <p>
     * Builds a new repository.
     * </p>
     *
     * @param bucket the bucket
     * @throws IOException if the repository is not able to initialize the views
     */
    protected CouchbaseReactvityRepository(final Bucket bucket) throws IOException {
        this.bucket = bucket.async();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Publisher<T> findArtifactFromView(final ArtifactView view, final Function<Artifact, T> mapper) {
        final ArtifactViewQuery artifactViewQuery = ArtifactViewQueryFactory.INSTANCE.create(view);

        return RxReactiveStreams.toPublisher(artifactViewQuery.query(bucket).map(mapper::apply));
    }

    /**
     * <p>
     * Generates an error for the given result.
     * </p>
     *
     * @param asyncQueryResult the result
     * @return the observable error
     */
    static Observable<Artifact> error(final AsyncN1qlQueryResult asyncQueryResult) {
        return asyncQueryResult.errors().flatMap(
                jsonErrors -> Observable.<Artifact>error(
                        new IllegalStateException(
                                jsonErrors.getInt("code") + ": " + jsonErrors.getString("msg"))));
    }
}
