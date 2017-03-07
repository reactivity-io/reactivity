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


package io.reactivity.core.broadcaster.web;

import io.reactivity.core.broadcaster.service.EventService;
import io.reactivity.core.broadcaster.session.ReactivitySessionScope;
import io.reactivity.core.lib.event.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * This controller serves event streams over REST API.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@RestController
@Scope(ReactivitySessionScope.REACTIVITY_SESSION)
public class EventController {

    /**
     * The service.
     */
    private final EventService eventService;

    /**
     * Current authentication.
     */
    private final Authentication authentication;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param eventService the event service
     * @param authentication the authentication
     */
    @Autowired
    public EventController(final EventService eventService, final Authentication authentication) {
        this.eventService = eventService;
        this.authentication = authentication;
    }

    /**
     * <p>
     * Loads all the artifacts matching the given view but with the specified period.
     * </p>
     *
     * @param viewId the view ID
     * @param limit maximum number of returned artifacts
     * @param maxAge highest possible age for an artifact
     * @return the event flux
     */
    @GetMapping("/load/artifacts/{viewId}/limit/{limit}/maxage/{maxAge}")
    public Object loadArtifactsLteMaxAge(
            @PathVariable(name = "viewId") final String viewId,
            @PathVariable(name = "limit") final int limit,
            @PathVariable(name = "maxAge") final long maxAge) {
        return eventService.loadArtifactsLteMaxAge(viewId, limit, maxAge);
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
    @GetMapping("/load/artifacts/{viewId}/limit/{limit}/minage/{minAge}")
    public Object loadArtifactsGteMinAge(
            @PathVariable(name = "viewId") final String viewId,
            @PathVariable(name = "limit") final int limit,
            @PathVariable(name = "minAge") final long minAge) {
        return eventService.loadArtifactsGteMinAge(viewId, limit, minAge);
    }

    /**
     * <p>
     * Loads the organizations as specified by {@link EventService#loadOrganizations(String)}.
     * </p>
     *
     * @return the organization event flux
     */
    @GetMapping("/load/organizations")
    public Object loadOrganizations() {
        // Retrieve the organizations: member ID can be an arbitrary value as it is currently mocked
        return eventService.loadOrganizations(authentication.getName());
    }

    /**
     * <p>
     * Subscribes to a particular {@link Organization} as specified by {@link EventService#subscribe(String)}.
     * </p>
     *
     * @param organizationId the organization ID
     * @return the event flux
     */
    @GetMapping("/subscribe/{organizationId}")
    public Object subscribe(@PathVariable(name = "organizationId") final String organizationId) {
        return eventService.subscribe(organizationId);
    }
}
