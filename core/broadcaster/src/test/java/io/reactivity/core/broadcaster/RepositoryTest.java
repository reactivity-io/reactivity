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


package io.reactivity.core.broadcaster;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import io.reactivity.core.broadcaster.repository.couchbase.MockCouchbaseReactivityRepository;
import io.reactivity.core.lib.ReactivityEntity;
import io.reactivity.core.lib.Version;
import io.reactivity.core.lib.ViewType;
import io.reactivity.core.lib.event.ArtifactView;
import io.reactivity.core.lib.event.Period;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

/**
 * <p>
 * Some tests for the Reactivity repository.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(properties = "build.version=0.1.0")
public class RepositoryTest {

    /**
     * The bucket.
     */
    @Autowired
    private Bucket bucket;

    /**
     * Repository.
     */
    @Autowired
    private MockCouchbaseReactivityRepository couchbaseReactivityRepository;

    /**
     * Inserts 10 random artifacts to the repository.
     */
    @Test
    public void insertSomeArtifactsDocuments() {
        Mono.fromSupplier(() -> {
            final Map<String, Object> categories = new HashMap<>();
            final BiConsumer<String, String[]> populator = (key, values) -> {
                final String value = values[ThreadLocalRandom.current().nextInt(0, values.length)];

                if (value != null) {
                    categories.put(key, value);
                }
            };

            populator.accept("assignee", new String[]{"ndamie", "gdrouet", "asanchez", "qlevaslot", "cazelart", "fclety", "hazarian"});
            populator.accept("category", new String[]{"bug", "feature", "question"});
            populator.accept("priority", new String[]{"high", "low", "medium"});

            final String id = UUID.randomUUID().toString();
            categories.put("description", "Description of Artifact " + id);

            final JsonObject object = JsonObject.create()
                    .put("version", 10)
                    .put("snapshot", true)
                    .put("organization", "Organization/1")
                    .put("updated", System.currentTimeMillis())
                    .put("categories", JsonObject.from(categories));

            return bucket.insert(JsonDocument.create(id, object));
        }).repeat(10).subscribe();
    }

    /**
     * Tests version filtering.
     *
     * @throws InterruptedException if test fails
     */
    @Test
    public void testVersions() throws InterruptedException {
        final long start = System.currentTimeMillis();
        bucket.insert(JsonDocument.create(UUID.randomUUID().toString(), JsonObject.create()
                .put("version", 10)
                .put("snapshot", true)
                .put("organization", "Organization/1")
                .put("updated", start + 1)
                .put("categories", JsonObject.create())));

        bucket.insert(JsonDocument.create(UUID.randomUUID().toString(), JsonObject.create()
                .put("version", 10)
                .put("snapshot", false)
                .put("organization", "Organization/1")
                .put("updated", start + 1)
                .put("categories", JsonObject.create())));

        bucket.insert(JsonDocument.create(UUID.randomUUID().toString(), JsonObject.create()
                .put("version", 9)
                .put("snapshot", true)
                .put("organization", "Organization/1")
                .put("updated", start + 1)
                .put("categories", JsonObject.create())));

        bucket.insert(JsonDocument.create(UUID.randomUUID().toString(), JsonObject.create()
                .put("version", 9)
                .put("snapshot", false)
                .put("organization", "Organization/1")
                .put("updated", start + 1)
                .put("categories", JsonObject.create())));

        bucket.insert(JsonDocument.create(UUID.randomUUID().toString(), JsonObject.create()
                .put("version", 11)
                .put("snapshot", true)
                .put("organization", "Organization/1")
                .put("updated", start + 1)
                .put("categories", JsonObject.create())));

        bucket.insert(JsonDocument.create(UUID.randomUUID().toString(), JsonObject.create()
                .put("version", 11)
                .put("snapshot", false)
                .put("organization", "Organization/1")
                .put("updated", start + 1)
                .put("categories", JsonObject.create())));

        // Wait a little bit and let couchbase store the document
        Thread.sleep(300L);

        final List<ReactivityEntity> list = Flux.from(couchbaseReactivityRepository.findArtifactFromView(
                new ArtifactView(
                        new Version("0.1.0-SNAPSHOT"),
                        UUID.randomUUID().toString(),
                        System.currentTimeMillis(),
                        "Organization/1",
                        "Last artifacts created in your organization",
                        new Period(start, start + 2, null, null),
                        ViewType.LIST.name()), e -> e))
                .collectList()
                .block();

        Assert.assertEquals(2, list.size());

        list.forEach(c -> Assert.assertEquals(5, c.getVersion().getSemver().length()));
        list.forEach(c -> Assert.assertFalse(c.getVersion().isSnapshot()));
    }
}
