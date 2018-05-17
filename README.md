[//]: # " Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Public License v. 2.0, which is available at "
[//]: # " http://www.eclipse.org/legal/epl-2.0. "
[//]: # "  "
[//]: # " This Source Code may also be made available under the following Secondary "
[//]: # " Licenses when the conditions for such availability set forth in the "
[//]: # " Eclipse Public License v. 2.0 are satisfied: GNU General Public License, "
[//]: # " version 2 with the GNU Classpath Exception, which is available at "
[//]: # " https://www.gnu.org/software/classpath/license.html. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 "

This module interfaces with the TLS implementation of Oracle JDK8 to
provide an implementation of ALPN.  The module was written for use in
Grizzly, but is intended as a general purpose solution to allow JDK8
based software to use ALPN.

# How Grizzly Uses This Module

Grizzly has the concept of an
[`AddOn`](https://github.com/eclipse-ee4j/grizzly/blob/master/modules/http-server/src/main/java/org/glassfish/grizzly/http/server/AddOn.java).
This facility allows Grizzly to be extended using an implementation of
the Chain of Responsibility design pattern.  In general, an `AddOn`
implementation will insert one or more `Filter` implementations into the
`FilterChain` which is used to process HTTP requests and cause HTTP
responses to be sent.

Grizzly itself uses this `AddOn` concept to provide HTTP/2 support, in
the form of
[`Http2AddOn`](https://github.com/eclipse-ee4j/grizzly/blob/master/modules/http2/src/main/java/org/glassfish/grizzly/http2/Http2AddOn.java)
In addition to registering filters for HTTP/2 processing, this `AddOn`
implementation registers a callback withthe ALPN extension to insert
itself into the
[SSL Handshake](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_7.1.0/com.ibm.mq.doc/sy10660_.htm)
process

## Programmatic Configuration

`AddOn` has a `setup` method that is called to allow the implementation
to do whatever one-time setup is required to make things work.  The
`setup` override in the `Http2AddOn` takes the following actions if the
current connection is secure (that is, it is supposed to be using ALPN).

* Use the `addHandshakeListener` method of the existing
  [`SSLBaseFilter`](https://github.com/eclipse-ee4j/grizzly/blob/master/modules/grizzly/src/main/java/org/glassfish/grizzly/ssl/SSLBaseFilter.java)
  to cause an ALPN specific SSL handshake listener to be added to the
  existing list of listeners that are invoked when an ssl handshake
  happens.

    Grizzly's handshake listener has several methods, but the only one
    used is `onStart`.  This obtains the JDK `SSLEngine` and takes
    different action depending on the return from its
    [`getUseClientMode()`](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLEngine.html#getUseClientMode--)
    method.  The `grizzly-npn` module provides
    [`AlpnClientNegotiator`](https://github.com/eclipse-ee4j/grizzly-npn/blob/master/api/src/main/java/org/glassfish/grizzly/npn/AlpnClientNegotiator.java)
    and
    [`AlpnServerNegotiator`](https://github.com/eclipse-ee4j/grizzly-npn/blob/master/api/src/main/java/org/glassfish/grizzly/npn/AlpnServerNegotiator.java)
    interfaces for these two cases.  Please see the interfaces for the
    specification of the required actions.

    If `getUseClientMode` is true,

    * use the grizzly `addCloseListener` facility to install a listener
    that calls
    [`NegotiationSupport`](https://github.com/eclipse-ee4j/grizzly-npn/blob/master/api/src/main/java/org/glassfish/grizzly/npn/NegotiationSupport.java)
    `.removeClientNegotiator()`.  This will ensure the client negotiator
    is removed when the connection closes.

    * Call `NegotiationSupport.addNegotiator` passing the `SSLEngine`
    and the client negotiator impl.

    If `getUseClientmode` is false,

    * do the same as above, but use the server negotiator impl.

    By the time `onStart` is invoked, the handshake listener will have
    been initialized with implementations of `AlpnClientNegotiator` and
    `AlpnServerNegotiator` by the `Http2AddOn`.

## Deployment Configuration

The above programmatic configuration steps will make it so the simple
act of including the module built from the `bootstrap` sub-module of
this repository in the bootclasspath of the JVM will enable ALPN support
in that JVM.
