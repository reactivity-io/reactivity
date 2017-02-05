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


package io.reactivity.core.broadcaster.session;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * <p>
 * A simple {@code Scope} that can be associated to {@link #REACTIVITY_SESSION}.
 * The implementation does nothing and is just used to create a new bean each
 * time a session is resolved for a HTTP request.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class ReactivitySessionScope implements Scope {

    /**
     * The scope name.
     */
    public static final String REACTIVITY_SESSION = "REACTIVITY_SESSION";

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String s, final ObjectFactory<?> objectFactory) {
        return objectFactory.getObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object remove(final String s) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDestructionCallback(final String s, final Runnable runnable) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object resolveContextualObject(final String s) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConversationId() {
        return null;
    }
}
