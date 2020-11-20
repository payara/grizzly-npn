/*
 * Copyright (c) 2014, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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
 *
 * Contributors:
 *   Payara Services - Make class JDK 9 Compatible
 */

package org.glassfish.grizzly.npn;

import java.util.List;
import java.util.function.BiFunction;

import javax.net.ssl.SSLEngine;

/**
 * <p>
 *
 * Called during the SSL handshake when the current {@code SSLEngine}'s
 * {@code getUseClientMode} has returned {@code false}.  Implementations must be
 * thread safe.  For HTTP/2, implementations must recognize "h2" and "http/1.1"
 * protocol identifiers, without the quotes.
 *
 * <p>
 */
public interface AlpnServerNegotiator extends BiFunction<SSLEngine, List<String>, String> {

    /**
     * <p>
     *
     * Take the necessary actions to declare support for the above protocols
     * and return the selected protocol.
     *
     *
     * @param sslEngine the {@code SSLEngine} for this connection.
     * @param clientProtocols the available client protocols
     * @return the selected protocol.
     */
    String selectProtocol(SSLEngine sslEngine, String[] clientProtocols);

    default String apply(SSLEngine sslEngine, List<String> clientProtocols) {
        return selectProtocol(sslEngine, clientProtocols.toArray(new String[0]));
    }

}
