package org.commonjava.test.http;

import static org.commonjava.test.http.client.HttpClientAssertions.assertContentLength;
import static org.commonjava.test.http.client.HttpClientAssertions.assertContentType;
import static org.commonjava.test.http.client.HttpClientAssertions.assertResponseContent;
import static org.commonjava.test.http.client.HttpClientAssertions.assertStatus;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.log4j.Level;
import org.commonjava.test.http.client.HttpClientFixture;
import org.commonjava.test.http.server.HttpServerFixture;
import org.commonjava.test.http.server.impl.FileContent;
import org.commonjava.util.logging.Log4jUtil;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class HttpFixturesWithDefaultTriesRandomPortTest
{

    @Rule
    public HttpServerFixture server = new HttpServerFixture();

    @Rule
    public HttpClientFixture client = new HttpClientFixture();

    @BeforeClass
    public static void setup()
    {
        Log4jUtil.configure( Level.DEBUG );
    }

    @Test
    public void retrieveSimpleFile()
        throws Throwable
    {
        final File f = getClasspathFile( "simple-file.txt" );
        final String content = FileUtils.readFileToString( f );

        final FileContent fc = new FileContent( f );

        final String path = "/simple/file.txt";
        server.addContent( path, fc );

        final HttpResponse response = client.get( server.getAddress(), server.getPort(), path );

        assertStatus( response, HttpStatus.SC_OK );

        assertContentLength( response, content.getBytes().length );
        assertContentType( response, "text/plain" );
        assertResponseContent( response, content );
    }

    private File getClasspathFile( final String path )
    {
        final URL resource = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResource( path );
        if ( resource == null )
        {
            fail( "Cannot find classpath resource: " + path );
        }

        final File f = new File( resource.getPath() );
        return f;
    }

}
