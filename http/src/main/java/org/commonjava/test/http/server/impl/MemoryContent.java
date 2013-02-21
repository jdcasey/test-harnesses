package org.commonjava.test.http.server.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
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
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy( stream, baos );

        this.content = baos.toByteArray();
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
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream( baos );
        ps.print( content );
        ps.flush();
        ps.close();

        this.content = baos.toByteArray();
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
    public byte[] getBytes()
    {
        return content;
    }

}
