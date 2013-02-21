package org.commonjava.test.http.server.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;

import org.commonjava.test.http.server.Content;

public class FileContent
    implements Content
{

    private final File file;

    private String contentType;

    public FileContent( final File file )
    {
        this( file, null );
    }

    public FileContent( final File file, final String contentType )
    {
        this.file = file;
        if ( contentType == null )
        {
            final MimetypesFileTypeMap map = new MimetypesFileTypeMap();
            this.contentType = map.getContentType( file );
        }
        else
        {
            this.contentType = contentType;
        }
    }

    @Override
    public long getLastModified()
    {
        return file.lastModified();
    }

    @Override
    public long getContentLength()
    {
        return file.length();
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
        return new FileInputStream( file );
    }

}
