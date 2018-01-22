/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.grizzly.npn;

import javax.net.ssl.SSLEngine;
import java.util.LinkedHashSet;

/**
 * Provides a set of callbacks that will be invoked when negotiation
 * NPN on the server side.
 */
public interface ServerSideNegotiator {

    /**
     * @return a {@link LinkedHashSet} of protocols that will be advertised
     *  to the client.
     */
    public LinkedHashSet<String> supportedProtocols(final SSLEngine engine);

    /**
     * Invoked when the client selects one of the supported protocols.
     */
    public void onSuccess(final SSLEngine engine, final String protocol);

    /**
     * Invoked when the client selects none of the supported protocols.
     */
    public void onNoDeal(final SSLEngine engine);

}
