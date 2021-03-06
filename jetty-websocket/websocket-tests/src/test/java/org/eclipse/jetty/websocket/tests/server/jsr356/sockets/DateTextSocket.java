//
//  ========================================================================
//  Copyright (c) 1995-2017 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.tests.server.jsr356.sockets;

import java.io.IOException;
import java.util.Date;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.toolchain.test.StackUtils;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.tests.jsr356.coders.DateDecoder;
import org.eclipse.jetty.websocket.tests.jsr356.coders.DateEncoder;

@ServerEndpoint(value = "/echo/beans/date", decoders = { DateDecoder.class }, encoders = { DateEncoder.class })
public class DateTextSocket
{
    private static final Logger LOG = Log.getLogger(DateTextSocket.class);

    private Session session;

    @OnOpen
    public void onOpen(Session session)
    {
        this.session = session;
    }

    // The decoder declared in the @ServerEndpoint will be used
    @OnMessage
    public void onMessage(Date d) throws IOException
    {
        if (d == null)
        {
            session.getAsyncRemote().sendText("Error: Date is null");
        }
        else
        {
            // The encoder declared in the @ServerEndpoint will be used
            session.getAsyncRemote().sendObject(d);
        }
    }

    @OnError
    public void onError(Throwable cause) throws IOException
    {
        LOG.warn("Error",cause);
        session.getBasicRemote().sendText("Exception: " + StackUtils.toString(cause));
    }
}
