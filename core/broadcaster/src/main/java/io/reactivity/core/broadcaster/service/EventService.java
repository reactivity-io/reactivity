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


package io.reactivity.core.broadcaster.service;

import io.reactivity.core.broadcaster.repository.ReactivityRepository;
import io.reactivity.core.lib.ReactivityEntity;
import io.reactivity.core.lib.event.ArtifactView;
import io.reactivity.core.lib.event.Error;
import io.reactivity.core.lib.event.Event;
import io.reactivity.core.lib.event.EventType;
import io.reactivity.core.lib.event.Organization;
import io.reactivity.core.lib.event.Period;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>
 * Reactivity service providing streams of events available in the platform.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@Service
public class EventService {

    /**
     * The repository storing the data.
     */
    @Autowired
    private ReactivityRepository repository;

    /**
     * Flux decorator.
     */
    @Autowired
    private FluxDecorator fluxDecorator;

    /**
     * <p>
     * Loads all the artifacts matching the given view but with the specified period.
     * </p>
     *
     * @param limit maximum number of returned artifacts
     * @param maxAge highest possible age for an artifact
     * @param viewId the view ID
     * @return the event flux
     */
    public Flux<Event<ReactivityEntity>> loadArtifactsLteMaxAge(final String viewId, final int limit, final long maxAge) {

        // When the view is retrieve, update the period state to select artifacts according to parameters
        return loadArtifacts(viewId, v -> {
            if (v instanceof ArtifactView) {
                final ArtifactView cast = ArtifactView.class.cast(v);
                final Period p = cast.getPeriod();

                return new ArtifactView(cast, new Period(p.getFrom(), maxAge, limit, p.getCategory()));
            }

            return v;
        });
    }

    /**
     * <p>
     * Loads all the artifacts matching the given view but with the specified period.
     * </p>
     *
     * @param viewId the view ID
     * @param limit maximum number of returned artifacts
     * @param minAge lowest possible age for an artifact
     * @return the event flux
     */
    public Flux<Event<ReactivityEntity>> loadArtifactsGteMinAge(final String viewId, final int limit, final long minAge) {

        // When the view is retrieve, update the period state to select artifacts according to parameters
        return loadArtifacts(viewId, v -> {
            if (v instanceof ArtifactView) {
                final ArtifactView cast = ArtifactView.class.cast(v);
                final Period p = cast.getPeriod();

                return new ArtifactView(cast, new Period(minAge, p.getTo(), limit, p.getCategory()));
            }

            return v;
        });
    }

    /**
     * <p>
     * Loads the organizations associated to the user identified with the given user ID.
     * </p>
     *
     * @param userId the user ID
     * @return the organization event flux
     */
    public Flux<Event<ReactivityEntity>> loadOrganizations(final String userId) {
        // Retrieve the organizations: member ID can be an arbitrary value as it is currently mocked
        return Flux.from(repository.findOrganizationsWithMember(userId, EventType.READ_ORGANIZATION::newEvent));
    }

    /**
     * <p>
     * Subscribes to a particular {@link Organization} and sends it to the response with its {@link ArtifactView views}
     * and corresponding {@link io.reactivity.core.lib.event.Artifact artifacts}.
     * </p>
     *
     * @param organizationId the organization ID
     * @return the event flux
     */
    public Flux<Event<ReactivityEntity>> subscribe(final String organizationId) {
        return Flux.create(emitter -> {
            // Complete the emitter once all flux have been completed
            final AtomicInteger fluxCount = new AtomicInteger(1);
            final Consumer<Throwable> onError = e -> emitter.complete();
            final Runnable finishFlux = () -> {
                if (fluxCount.decrementAndGet() == 0) {
                    emitter.complete();
                }
            };

            final Consumer<Event<ReactivityEntity>> emit = e -> {
                fluxCount.incrementAndGet();
                emitter.next(e);
            };

            // When an organization is received...
            Flux.from(repository.findOrganizationsWithMember(organizationId, o -> o)).doOnError(onError).subscribe(o -> {
                // ... retrieve the associated views
                final Publisher<Event<ReactivityEntity>> viewsPub =
                        repository.findViewsFromOrganization(o.getId(), EventType.READ_VIEW::newEvent);
                final Flux<Event<ReactivityEntity>> views = fluxDecorator.decorate(
                        Flux.from(viewsPub)
                                .doOnError(onError)
                                .doOnComplete(finishFlux), "findViewsFromOrganization(" + o.getId() + ")");

                // When a view is received ...
                views.subscribe(v -> {
                    // ... emit it ...
                    emit.accept(v);

                    if (v.getData() instanceof ArtifactView) {
                        final ArtifactView view = ArtifactView.class.cast(v.getData());
                        final Publisher<Event<ReactivityEntity>> artifactsPub =
                                repository.findArtifactFromView(view, EventType.READ_ARTIFACT::newEvent);

                        // ... and retrieve the associated artifacts
                        fluxDecorator.decorate(
                                Flux.from(artifactsPub)
                                .doOnError(onError)
                                .doOnComplete(finishFlux), "findArtifactFromView(" + view + ")")
                                .subscribe(emitter::next);
                    }
                });
            });
        });
    }


    /**
     * <p>
     * Loads all the artifacts matching the given view but with the period adapter.
     * </p>
     *
     * @param viewId the view ID
     * @param periodAdapter a function that changes the period
     * @return the event flux
     */
    private Flux<Event<ReactivityEntity>> loadArtifacts(final String viewId,
                                                        final Function<ReactivityEntity, ReactivityEntity> periodAdapter) {
        // Load the view and substitute the item to the corresponding artifacts
        return Flux.from(repository.findViewById(viewId, periodAdapter))
                .concatMap(v -> {
                    if (v instanceof ArtifactView) {
                        final ArtifactView cast = ArtifactView.class.cast(v);
                        return repository.findArtifactFromView(cast, EventType.READ_ARTIFACT::newEvent);
                    } else if (v instanceof Error) {
                        return Flux.just(EventType.ERROR.newEvent(v));
                    }

                    // Handle artifact view or error only
                    throw new IllegalArgumentException(
                            String.format("Unsupported ReactivityEntity in this mapper: %s", v.getClass().getName()));
                });
    }
}
