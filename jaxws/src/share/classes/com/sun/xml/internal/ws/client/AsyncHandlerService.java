/*
 * Portions Copyright 2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.xml.internal.ws.client;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import java.rmi.server.UID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class AsyncHandlerService {

    private AsyncHandler _handler;
    private UID _uid;
    private Executor _executor;
    private WSFuture wsfuture;
    private Response response;

    public AsyncHandlerService(AsyncHandler handler, Executor executor) {
        _uid = new UID();
        _handler = handler;
        _executor = executor;
    }

    public synchronized UID getUID() {
        return _uid;
    }

    public void executeWSFuture() {

        _executor.execute((Runnable) wsfuture);
    }

    public WSFuture<Object> setupAsyncCallback(final Response<Object> result) {
        response = result;

        wsfuture = new WSFuture<Object>(new Callable<Object>() {

            public Object call() throws Exception {
                _handler.handleResponse(response);
                return null;
            }
        });
        return wsfuture;
    }
}
