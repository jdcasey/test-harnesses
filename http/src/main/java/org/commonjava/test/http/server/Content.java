package org.commonjava.test.http.server;

import java.io.IOException;
import java.io.InputStream;

public interface Content
{

    long getLastModified();

    long getContentLength();

    String getContentType();

    InputStream getStream()
        throws IOException;

}
