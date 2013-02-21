package org.commonjava.test.http.server.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.commonjava.test.http.server.Content;

public class MemoryContent
    implements Content
{

    private final byte[] content;

    private final String contentType;

    private final long lastModified;

    public MemoryContent( final InputStream stream )
        throws IOException
    {
        this( stream, null, null );
    }

    public MemoryContent( final InputStream stream, final String contentType, final Long lastModified )
        throws IOException
    {
        this.content = IOUtils.toByteArray( stream );
        this.contentType = contentType == null ? "application/octet-stream" : contentType;
        this.lastModified = lastModified == null ? System.currentTimeMillis() : lastModified;
    }

    public MemoryContent( final byte[] content )
    {
        this( content, null, null );
    }

    public MemoryContent( final byte[] content, final String contentType, final Long lastModified )
    {
        this.content = content;
        this.contentType = contentType == null ? "application/octet-stream" : contentType;
        this.lastModified = lastModified == null ? System.currentTimeMillis() : lastModified;
    }

    public MemoryContent( final String content )
    {
        this( content, null, null );
    }

    public MemoryContent( final String content, final String contentType, final Long lastModified )
    {
        this.content = content.getBytes();
        this.contentType = contentType == null ? "text/plain" : contentType;
        this.lastModified = lastModified == null ? System.currentTimeMillis() : lastModified;
    }

    @Override
    public long getLastModified()
    {
        return lastModified;
    }

    @Override
    public long getContentLength()
    {
        return content.length;
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }

    @Override
    public InputStream getStream()
    {
        return new ByteArrayInputStream( content );
    }

}
