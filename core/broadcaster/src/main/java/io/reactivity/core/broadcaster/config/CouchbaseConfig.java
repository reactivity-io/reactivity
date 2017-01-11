/*
 * The MIT License (MIT) Copyright (c) 2016 The reactivity authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package io.reactivity.core.broadcaster.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * This configuration initializes couchbase connection.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@Configuration
@ConfigurationProperties(prefix = "reactivity.couchbase")
public class CouchbaseConfig {

//    private String[] nodes = new String[] { "127.0.0.1" } ;
    private String[] nodes = new String[] { "ec2-35-160-191-149.us-west-2.compute.amazonaws.com" } ;

    /**
     * <p>
     * The application won't be able to use this bucket until an index is created.
     * </p>
     *
     * @return the {@code Bucket}
     */
//    @Bean
//    Bucket sync() {
//        final CouchbaseCluster cluster = CouchbaseCluster.create(nodes);
//        final Bucket bucket = cluster.openBucket("artifact");
//        bucket.bucketManager().createN1qlPrimaryIndex(true, false);
//
//        return bucket;
//    }
    @Bean
    Bucket sync() {
        final CouchbaseCluster cluster = CouchbaseCluster.create(nodes);
        final Bucket bucket = cluster.openBucket("nana");
        bucket.bucketManager().createN1qlPrimaryIndex(true, false);

        return bucket;
    }

    /**
     * <p>
     * Sets the couchbase nodes with a comma-separated list of IPs.
     * </p>
     *
     * @param nodes the bootstrap nodes
     */
    public void setNodes(final String[] nodes) {
        this.nodes = nodes;
    }
}
