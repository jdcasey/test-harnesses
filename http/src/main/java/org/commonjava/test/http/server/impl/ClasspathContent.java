package org.commonjava.test.http.server.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.commonjava.test.http.server.Content;

public class ClasspathContent
    implements Content
{

    private String contentType;

    private final URL resource;

    private final long lastModified;

    private long contentLength;

    public ClasspathContent( final String path )
        throws IOException
    {
        this( path, null, null );
    }

    public ClasspathContent( final String path, final String contentType, final Long lastModified )
        throws IOException
    {
        this.lastModified = lastModified == null ? System.currentTimeMillis() : lastModified;

        resource = Thread.currentThread()
                         .getContextClassLoader()
                         .getResource( path );

        if ( resource == null )
        {
            throw new RuntimeException( "Cannot find classpath resource: " + path );
        }

        if ( contentType == null )
        {
            final MimetypesFileTypeMap map = new MimetypesFileTypeMap();
            final File f = new File( resource.getPath() );

            this.contentType = map.getContentType( f );
        }
        else
        {
            this.contentType = contentType;
        }

        InputStream stream = null;
        try
        {
            stream = resource.openStream();

            final CountingOutputStream cos = new CountingOutputStream( new OutputStream()
            {
                @Override
                public void write( final int b )
                    throws IOException
                {
                }
            } );

            IOUtils.copy( stream, cos );
            this.contentLength = cos.getByteCount();
        }
        finally
        {
            IOUtils.closeQuietly( stream );
        }
    }

    @Override
    public long getLastModified()
    {
        return lastModified;
    }

    @Override
    public long getContentLength()
    {
        return contentLength;
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }

    @Override
    public InputStream getStream()
        throws IOException
    {
        return resource.openStream();
    }

}
