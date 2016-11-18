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
import io.reactivity.core.broadcaster.config.CouchbaseConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.HashMap;
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
@ContextConfiguration(classes = CouchbaseConfig.class)
public class RepositoryTest {

    /**
     * The bucket.
     */
    @Autowired
    private Bucket bucket;

    /**
     * Inserts 1000 random artifacts to the repository.
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
                    .put("version", "0.1.0-SNAPSHOT")
                    .put("organization", "Organization/1")
                    .put("updated", System.currentTimeMillis())
                    .put("categories", JsonObject.from(categories));

            return bucket.insert(JsonDocument.create(id, object));
        }).repeat(1000).subscribe();
    }
}
