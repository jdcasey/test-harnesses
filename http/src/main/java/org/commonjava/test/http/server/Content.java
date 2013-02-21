package org.commonjava.test.http.server;

import java.io.IOException;

public interface Content
{

    long getLastModified();

    long getContentLength();

    String getContentType();

    byte[] getBytes()
        throws IOException;

}
