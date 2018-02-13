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
 * NPN on the client side.
 */
public interface ClientSideNegotiator {

    /**
     * @return <code>true</code> if this NPN negotiation should occur, otherwise
     *  <code>false</code>.
     */
    public boolean wantNegotiate(final SSLEngine engine);

    /**
     * Select and return a single protocol from the provided set of protocols.
     */
    public String selectProtocol(final SSLEngine engine,
                                 final LinkedHashSet<String> protocols);

    /**
     * This will be invoked if no protocols are returned from the server or
     * selectProtocol returns <code>null</code>.
     */
    public void onNoDeal(final SSLEngine engine);

}
