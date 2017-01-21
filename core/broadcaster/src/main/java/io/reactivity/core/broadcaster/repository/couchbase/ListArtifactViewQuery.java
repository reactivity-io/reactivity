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
import com.couchbase.client.java.document.json.JsonObject;

import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.couchbase.client.java.query.Select;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.dsl.Expression;
import com.couchbase.client.java.query.dsl.Sort;
import com.couchbase.client.java.query.dsl.path.LimitPath;
import io.reactivity.core.lib.ReactivityEntity;
import io.reactivity.core.lib.Version;
import io.reactivity.core.lib.event.Artifact;
import io.reactivity.core.lib.event.ArtifactView;
import io.reactivity.core.lib.event.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.Collections;

/**
 * <p>
 * This {@link ArtifactViewQuery} supports queries for {@link ArtifactView} of type
 * {@link io.reactivity.core.lib.ViewType#LIST}.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
class ListArtifactViewQuery implements ArtifactViewQuery {

    /**
     * Fields in the SELECT clause of the query.
     */
    private static final String FIELDS = "version, snapshot, meta(artifact).id, categories, updated";

    /**
     * The view to use.
     */
    private final ArtifactView view;

    /**
     * The logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * The application version.
     */
    private int version;

    /**
     * Indicates if the application is a snapshot or not.
     */
    private boolean isSnapshot;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param view the view corresponding to the executed query
     * @param version the version of the application build
     * @param isSnapshot {@code true} if the application is a snapshot, {@code false} otherwise
     */
    ListArtifactViewQuery(final ArtifactView view, final int version, final boolean isSnapshot) {
        this.view = view;
        this.version = version;
        this.isSnapshot = isSnapshot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Observable<ReactivityEntity> query(final AsyncBucket bucket) {

        // Prepare the statement and parameters
        final JsonObject params = JsonObject.create().put("organization", view.getOrganization());
        final Period period = view.getPeriod();
        final String timestampField = period.getCategory() != null ? "categories." + period.getCategory() : "updated";

        Expression expression = Expression.x("organization").eq(Expression.x("$organization"));

        if (accept(period.getFrom())) {
            expression = expression.and(Expression.x(timestampField).gte(period.getFrom()));
        }

        if (accept(period.getTo())) {
            expression = expression.and(Expression.x(timestampField).lte(period.getTo()));
        }

        expression = expression.and(Expression.x("version")).lte(version);

        if (!isSnapshot) {
            expression = expression.and(Expression.x("snapshot").eq(false));
        }

        final LimitPath limitPath = Select.select(FIELDS)
                .from(bucket.name())
                .where(expression)
                .orderBy(Sort.desc(timestampField));

        // Build the query
        final Statement statement = period.getLimit() != null ? limitPath.limit(period.getLimit()) : limitPath;
        final ParameterizedN1qlQuery query = N1qlQuery.parameterized(statement, params);

        // Execute and map to an Observable of Artifact
        return bucket.query(query).flatMap(result -> result.parseSuccess() ?
                // Map each internal JSON object to an Artifact and handles statement failure
                result.rows().map(this::toArtifact) : CouchbaseReactvityRepository.error(result, log));
    }

    /**
     * <p>
     * Creates an artifact from the given row result.
     * </p>
     *
     * @param row the row result
     * @return the mapped artifact
     */
    private Artifact toArtifact(final AsyncN1qlQueryRow row) {
        final JsonObject o = JsonObject.class.cast(row.value());
        return new Artifact(new Version(o.getBoolean("snapshot"), o.getInt("version")),
                o.getString("id"),
                o.getLong("updated"),
                Collections.singletonList(view.getId()),
                o.getObject("categories").toMap());
    }

    /**
     * <p>
     * Indicates if the given {@code Long} is not {@code null} and not negative.
     * </p>
     *
     * @param value long value
     * @return {@code true} if the value is greater or equals to 0, {@code false} otherwise
     */
    private boolean accept(final Long value) {
        return value != null && value.compareTo(0L) > -1;
    }
}
