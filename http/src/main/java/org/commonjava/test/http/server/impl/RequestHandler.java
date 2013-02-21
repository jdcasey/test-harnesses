package org.commonjava.test.http.server.impl;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.commonjava.test.http.server.Content;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class RequestHandler
    extends AbstractHandler
{

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";

    public static final int HTTP_CACHE_SECONDS = 60;

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final String CONTENT_TYPE = "Content-Type";

    private final Map<String, Content> contentMap;

    public RequestHandler( final Map<String, Content> contentMap )
    {
        this.contentMap = contentMap;
    }

    @Override
    public void handle( final String target, final Request baseRequest, final HttpServletRequest request,
                        final HttpServletResponse response )
        throws IOException, ServletException
    {
        final Content content = contentMap.get( target );
        if ( content == null )
        {
            response.setStatus( HttpServletResponse.SC_NOT_FOUND );
        }
        else
        {
            response.setStatus( HttpServletResponse.SC_OK );
            response.setHeader( CONTENT_LENGTH, Long.toString( content.getContentLength() ) );
            response.setHeader( CONTENT_TYPE, content.getContentType() );
            IOUtils.copy( content.getStream(), response.getOutputStream() );
        }

        baseRequest.setHandled( true );
    }

}
