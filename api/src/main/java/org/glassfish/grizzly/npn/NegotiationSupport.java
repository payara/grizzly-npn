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
// Portions Copyright [2022] [Payara Foundation and/or its affiliates]
package org.glassfish.grizzly.npn;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class to register, obtain, and/or remove Client/Server NPN/ALPN
 * negotiator instances.
 */
public class NegotiationSupport {

    private static final ConcurrentHashMap<String, ServerSideNegotiator> serverSideNegotiators =
            new ConcurrentHashMap<>(4);
    private static final ConcurrentHashMap<String, ClientSideNegotiator> clientSideNegotiators =
                new ConcurrentHashMap<>(4);
    private static final ConcurrentHashMap<String, AlpnServerNegotiator> alpnServerNegotiators =
                new ConcurrentHashMap<>(4);
        private static final ConcurrentHashMap<String, AlpnClientNegotiator> alpnClientNegotiators =
                    new ConcurrentHashMap<>(4);

    /**
     * Add a {@link ServerSideNegotiator} that will be invoked when handshake
     * activity occurs against the specified {@link SSLEngine}.
     */
    public static void addNegotiator(final SSLEngine engine,
                                     final ServerSideNegotiator serverSideNegotiator) {
        serverSideNegotiators.putIfAbsent(generateKey(engine), serverSideNegotiator);
    }

    /**
     * Add a {@link ClientSideNegotiator} that will be invoked when handshake
     * activity occurs against the specified {@link SSLEngine}.
     */
    public static void addNegotiator(final SSLEngine engine,
                                     final ClientSideNegotiator clientSideNegotiator) {
        clientSideNegotiators.putIfAbsent(generateKey(engine), clientSideNegotiator);
    }

    /**
     * Add a {@link AlpnServerNegotiator} that will be invoked when handshake
     * activity occurs against the specified {@link SSLEngine}.
     */
    public static void addNegotiator(final SSLEngine engine,
                                     final AlpnServerNegotiator serverSideNegotiator) {
        alpnServerNegotiators.putIfAbsent(generateKey(engine), serverSideNegotiator);
    }

    /**
     * Add a {@link AlpnClientNegotiator} that will be invoked when handshake
     * activity occurs against the specified {@link SSLEngine}.
     */
    public static void addNegotiator(final SSLEngine engine,
                                     final AlpnClientNegotiator clientSideNegotiator) {
        alpnClientNegotiators.putIfAbsent(generateKey(engine), clientSideNegotiator);
    }

    /**
     * Disassociate the {@link ClientSideNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static ClientSideNegotiator removeClientNegotiator(final SSLEngine engine) {
        return clientSideNegotiators.remove(generateKey(engine));
    }

    /**
     * Disassociate the {@link AlpnClientNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static AlpnClientNegotiator removeAlpnClientNegotiator(final SSLEngine engine) {
        return alpnClientNegotiators.remove(generateKey(engine));
    }

    /**
     * Disassociate the {@link ServerSideNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static ServerSideNegotiator removeServerNegotiator(final SSLEngine engine) {
        return serverSideNegotiators.remove(generateKey(engine));
    }

    /**
     * Disassociate the {@link AlpnServerNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static AlpnServerNegotiator removeAlpnServerNegotiator(final SSLEngine engine) {
        return alpnServerNegotiators.remove(generateKey(engine));
    }

    /**
     * @return the {@link ServerSideNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static ServerSideNegotiator getServerSideNegotiator(final SSLEngine engine) {
        return serverSideNegotiators.get(generateKey(engine));
    }

    /**
     * @return the {@link ClientSideNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static ClientSideNegotiator getClientSideNegotiator(final SSLEngine engine) {
        return clientSideNegotiators.get(generateKey(engine));
    }

    /**
     * @return the {@link AlpnServerNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static AlpnServerNegotiator getAlpnServerNegotiator(final SSLEngine engine) {
        return alpnServerNegotiators.get(generateKey(engine));
    }

    /**
     * @return the {@link AlpnClientNegotiator} associated with the specified
     * {@link SSLEngine}.
     */
    public static AlpnClientNegotiator getAlpnClientNegotiator(final SSLEngine engine) {
        return alpnClientNegotiators.get(generateKey(engine));
    }

    /**
     * This generates a key for the SSLEngine
     * @param engine instance of type SSLEngine used for the ALPN implementation
     * @return String with the key formed with Class Name and hashCode
     */
    private static String generateKey(SSLEngine engine){
        if(engine != null) {
            return engine.getClass().getName() + "@" + Integer.toHexString(engine.hashCode());
        }
        return null;
    }

}
