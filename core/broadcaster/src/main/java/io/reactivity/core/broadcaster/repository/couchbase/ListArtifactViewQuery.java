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
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.dsl.Expression;
import com.couchbase.client.java.query.dsl.Sort;
import com.couchbase.client.java.query.dsl.path.LimitPath;
import io.reactivity.core.lib.event.Artifact;
import io.reactivity.core.lib.event.ArtifactView;
import io.reactivity.core.lib.event.Period;
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
    private static final String FIELDS = "version, meta(artifact).id, categories, updated";

    /**
     * The view to use.
     */
    private final ArtifactView view;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param view the view corresponding to the executed query
     */
    ListArtifactViewQuery(final ArtifactView view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Observable<Artifact> query(final AsyncBucket bucket) {

        // Prepare the statement and parameters
        final JsonObject params = JsonObject.create().put("organization", view.getOrganization());
        final Period period = view.getPeriod();
        final String timestampField = period.getCategory() != null ? "categories." + period.getCategory() : "updated";

        Expression expression = Expression.x("organization").eq(Expression.x("$organization"));

        if (period.getFrom() != null) {
            expression = expression.and(Expression.x(timestampField).gte(period.getFrom()));
        }

        if (period.getTo() != null) {
            expression = expression.and(Expression.x(timestampField).lte(period.getTo()));
        }

        final LimitPath limitPath = Select.select(FIELDS).from("artifact").where(expression).orderBy(Sort.desc(timestampField));
        final Statement statement = period.getLimit() != null ? limitPath.limit(period.getLimit()) : limitPath;

        // Build the query
        final ParameterizedN1qlQuery query = N1qlQuery.parameterized(statement, params);

        // Execute and map to an Observable of Artifact
        return bucket.query(query).flatMap(result -> result.parseSuccess() ?
                // Map each internal JSON object to an Artifact and handles statement failure
                result.rows().map(this::toArtifact) : CouchbaseReactvityRepository.error(result));
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
        return new Artifact(o.getString("version"),
                o.getString("id"),
                o.getLong("updated"),
                Collections.singletonList(view.getId()),
                o.getObject("categories").toMap());
    }
}
