/*
 * The MIT License (MIT) Copyright (c) 2017 The reactivity authors
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


package io.reactivity.core.broadcaster;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import io.reactivity.core.broadcaster.web.EventController;
import io.reactivity.core.lib.ReactivityEntity;
import io.reactivity.core.lib.Version;
import io.reactivity.core.lib.event.Artifact;
import io.reactivity.core.lib.event.ArtifactView;
import io.reactivity.core.lib.event.Error;
import io.reactivity.core.lib.event.Event;
import io.reactivity.core.lib.event.EventType;
import io.reactivity.core.lib.event.Member;
import io.reactivity.core.lib.event.Organization;
import io.reactivity.core.lib.event.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * <p>
 * Tests the mocked {@link EventController} and document associated responses.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EventController.class)
@AutoConfigureRestDocs(
        outputDir = "target/generated-snippets",
        uriHost = "your-reactivity-server",
        uriPort = 80
)
public class ApiTest {

    /**
     * Mocked {@link EventController}.
     */
    @MockBean
    private EventController controller;

    /**
     * The mock MVC.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests artifact with a max age.
     *
     * @throws Exception if test fails
     */
    @Test
    public void loadArtifactsLteMaxAgeTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newArtifactEvent()))
                .when(controller).loadArtifactsLteMaxAge(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong());

        mockMvc.perform(get("/load/artifacts/myView/limit/1/maxage/1"))
                .andExpect(status().isOk())
                .andDo(document("load-artifacts-lte-max-age-example",
                        responseFields(eventFieldDescriptors(artifactEventFieldDescriptors()))));
    }

    /**
     * Tests artifact with a max age that fail to load.
     *
     * @throws Exception if test fails
     */
    @Test
    public void loadArtifactsLteMaxAgeErrorTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newErrorEvent()))
                .when(controller).loadArtifactsLteMaxAge(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong());

        mockMvc.perform(get("/load/artifacts/myView/limit/1/maxage/1"))
                .andExpect(status().isOk())
                .andDo(document("load-artifacts-lte-max-age-error-example",
                        responseFields(errorEventFieldDescriptors())));
    }

    /**
     * Tests artifact with a min age.
     *
     * @throws Exception if test fails
     */
    @Test
    public void loadArtifactsGteMinAgeTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newArtifactEvent()))
                .when(controller).loadArtifactsGteMinAge(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong());

        mockMvc.perform(get("/load/artifacts/myView/limit/1/minage/1"))
                .andExpect(status().isOk())
                .andDo(document("load-artifacts-gte-min-age-example",
                        responseFields(eventFieldDescriptors(artifactEventFieldDescriptors()))));
    }

    /**
     * Tests artifact with a min age that fail to load.
     *
     * @throws Exception if test fails
     */
    @Test
    public void loadArtifactsGteMinAgeErrorTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newErrorEvent()))
                .when(controller).loadArtifactsGteMinAge(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong());

        mockMvc.perform(get("/load/artifacts/myView/limit/1/minage/1"))
                .andExpect(status().isOk())
                .andDo(document("load-artifacts-gte-min-age-error-example",
                        responseFields(eventFieldDescriptors(errorEventFieldDescriptors()))));
    }

    /**
     * Tests load organizations.
     *
     * @throws Exception if test fails
     */
    @Test
    public void loadOrganizationsTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newOrganizationEvent()))
                .when(controller).loadOrganizations();

        mockMvc.perform(get("/load/organizations"))
                .andExpect(status().isOk())
                .andDo(document("load-organizations-example",
                        responseFields(eventFieldDescriptors(
                                fieldWithPath("[].data.name").description("The organization name."),
                                fieldWithPath("[].data.picture").description("The base64 representation of the organization avatar."),
                                fieldWithPath("[].data.members").description("The organization's members."),
                                fieldWithPath("[].data.members[].id").description("The member ID."),
                                fieldWithPath("[].data.members[].role").description("The member role.")))));
    }

    /**
     * Tests organization load that fails.
     *
     * @throws Exception if test fails
     */
    @Test
    public void loadOrganizationErrorTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newErrorEvent()))
                .when(controller).loadOrganizations();

        mockMvc.perform(get("/load/organizations"))
                .andExpect(status().isOk())
                .andDo(document("load-organizations-error-example",
                        responseFields(errorEventFieldDescriptors())));
    }

    /**
     * Tests subscription.
     *
     * @throws Exception if test fails
     */
    @Test
    public void subscribeTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newViewEvent()))
                .when(controller).subscribe(ArgumentMatchers.anyString());

        mockMvc.perform(get("/subscribe/1"))
                .andExpect(status().isOk())
                .andDo(document("subscribe-example",
                        responseFields(eventFieldDescriptors(
                                fieldWithPath("[].data.name").description("The view name."),
                                fieldWithPath("[].data.organization").description("The organization ID."),
                                fieldWithPath("[].data.period").description("The period defined for that view."),
                                fieldWithPath("[].data.period.from").description("From when the period starts."),
                                fieldWithPath("[].data.period.to").description("When the period ends."),
                                fieldWithPath("[].data.period.limit").description("Limit the number of results (if 'from' or 'to' is undefined)."),
                                fieldWithPath("[].data.period.category").description("The optional category containing a date to test with the period (updated by default)."),
                                fieldWithPath("[].data.type").description("The type of view.")))));
    }

    /**
     * Tests error subscription.
     *
     * @throws Exception if test fails
     */
    @Test
    public void subscribeErrorTest() throws Exception {
        Mockito.doAnswer(invocation -> Collections.singletonList(newErrorEvent()))
                .when(controller).subscribe(ArgumentMatchers.anyString());

        mockMvc.perform(get("/subscribe/1"))
                .andExpect(status().isOk())
                .andDo(document("subscribe-error-example",
                        responseFields(errorEventFieldDescriptors())));
    }

    /**
     * <p>
     * Creates an organization event.
     * </p>
     *
     * @return the organization event
     */
    private Event<ReactivityEntity> newOrganizationEvent() {
        return EventType.READ_ORGANIZATION.newEvent(
                new Organization(
                        new Version("1.0.0"), UUID.randomUUID().toString(),
                        1L,
                        "my-org",
                        "",
                        Collections.singletonList(new Member(UUID.randomUUID().toString(), "ADMIN"))));
    }

    /**
     * <p>
     * Creates a view event.
     * </p>
     *
     * @return the view event
     */
    private Event<ReactivityEntity> newViewEvent() {
        return EventType.READ_VIEW.newEvent(
                new ArtifactView(
                        new Version("1.0.0"), UUID.randomUUID().toString(),
                        1L,
                        "my-org",
                        "my-view",
                        new Period(0L, 1L, 10, "dueDate"),
                        "LIST"));
    }

    /**
     * <p>
     * Creates an artifact event.
     * </p>
     *
     * @return the organization event
     */
    private Event<ReactivityEntity> newArtifactEvent() {
        return EventType.READ_ARTIFACT.newEvent(
                new Artifact(
                        new Version("1.0.0"), UUID.randomUUID().toString(),
                        1L,
                        Collections.singletonList("myView"),
                        new HashMap<>()));
    }

    /**
     * <p>
     * Creates an error event.
     * </p>
     *
     * @return the organization event
     */
    private Event<ReactivityEntity> newErrorEvent() {
        return EventType.ERROR.newEvent(new Error("An error message."));
    }

    /**
     * <p>
     * Returns an array containing the fields of any error message.
     * </p>
     *
     * @return the field descriptor array
     */
    private FieldDescriptor[] errorEventFieldDescriptors() {
        return eventFieldDescriptors(fieldWithPath("[].data.message").description("The message"));
    }

    /**
     * <p>
     * Builds an array of {@code DescriptorField} containing the fields always found in an {@link Event} and the
     * additional field related to the {@link ReactivityEntity} wrapped by this event.
     * </p>
     *
     * @param additionalFields the data fields
     * @return the data field with the wrapping event fields
     */
    private FieldDescriptor[] eventFieldDescriptors(final FieldDescriptor ... additionalFields) {
        final FieldDescriptor[] eventFields = new FieldDescriptor[] {
                fieldWithPath("[].version").description("The application version corresponding to the wrapped entity."),
                fieldWithPath("[].snapshot").description("If the application that generated this entity is a snapshot."),
                fieldWithPath("[].id").description("The entity ID."),
                fieldWithPath("[].event").description("The type of event."),
                fieldWithPath("[].updated").description("When the entity was updated."),
        };

        final FieldDescriptor[] retval = new FieldDescriptor[eventFields.length + additionalFields.length];
        System.arraycopy(eventFields, 0, retval, 0, eventFields.length);
        System.arraycopy(additionalFields, 0, retval, eventFields.length, additionalFields.length);

        return retval;
    }

    /**
     * <p>
     * Returns the field descriptors of an event wrapping an artifact.
     * </p>
     *
     * @return the field descriptors
     */
    private FieldDescriptor[] artifactEventFieldDescriptors() {
        return eventFieldDescriptors(
                fieldWithPath("[].data.views").description("The ID of views that are able to display the artifact."),
                fieldWithPath("[].data.categories").description("The categories (a simple key/value pair) defined in the artifact."));
    }
}
