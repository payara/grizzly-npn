/*
 * Copyright (c) 2014, 2017 Oracle and/or its affiliates. All rights reserved.
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

/**
 * <p>
 *
 * Called during the SSL handshake when the current {@code SSLEngine}'s
 * {@code getUseClientMode} has returned {@code true}.  Implementations must be
 * thread safe.
 *
 * <p>
 */
public interface AlpnClientNegotiator {

    /**
     * <p>
     *
     * Return the supported protocols.  For HTTP/2 this should be the two literal
     * strings "h2" and "http/1.1", without the quotes.  This method is called
     * by the underlying SSL framework.
     *
     * <p>
     *
     * @param sslEngine the {@code SSLEngine} for this connection.
     * @return A newly allocated String array of protocols supported.
     */
    String[] getProtocols(SSLEngine sslEngine);

    /**
     * <p>
     *
     * Inform the implementor which of the protocols returned from {@link #getProtocols(javax.net.ssl.SSLEngine)}
     * was actually selected.
     *
     * <p>
     *
     * For HTTP/2, if the argument is "h2", proceed to use the HTTP/2 protocol
     * for the remainder of this connection.  Otherwise, take the necessary
     * action to use HTTP/1.1.
     *
     * @param sslEngine the {@code SSLEngine} for this connection.
     * @param selectedProtocol The selected protocol.
     */

    void protocolSelected(SSLEngine sslEngine, String selectedProtocol);

}
