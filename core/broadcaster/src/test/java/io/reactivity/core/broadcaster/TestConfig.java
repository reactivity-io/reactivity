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

import io.reactivity.core.broadcaster.config.CouchbaseConfig;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * This configuration class detects environment variable.
 * This kind of support is provided by spring boot but the {@code SpringBootApplication} annotation is currently not used.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@Configuration
public class TestConfig extends CouchbaseConfig {

    /**
     * <p>
     * Builds a the default instance.
     * </p>
     */
    public TestConfig() {
        final String nodes = System.getenv("REACTIVITY_COUCHBASE_NODES");

        if (nodes != null) {
            setNodes(nodes.split(","));
        }
    }
}
