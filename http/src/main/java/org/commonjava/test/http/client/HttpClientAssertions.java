package org.commonjava.test.http.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

public final class HttpClientAssertions
{

    private HttpClientAssertions()
    {
    }

    public static void assertContentLength( final HttpResponse response, final long length )
    {
        assertSingleHeader( response, "Content-Length", Long.toString( length ) );
    }

    public static void assertContentType( final HttpResponse response, final String contentType )
    {
        assertSingleHeader( response, "Content-Type", contentType );
    }

    public static void assertSingleHeader( final HttpResponse response, final String name, final String value )
    {
        if ( value == null )
        {
            fail( "Cannot assert single header is present with null value!" );
        }

        final Header[] headers = response.getHeaders( name );
        assertThat( headers, notNullValue() );
        assertThat( headers.length, equalTo( 1 ) );
        assertThat( headers[0].getValue(), equalTo( value ) );
    }

    public static void assertResponseContent( final HttpResponse response, final String content )
        throws IllegalStateException, IOException
    {
        final String test = IOUtils.toString( response.getEntity()
                                                      .getContent() );

        assertThat( test, equalTo( content ) );
    }

    public static void assertStatus( final HttpResponse response, final int code )
    {
        assertThat( response.getStatusLine()
                            .getStatusCode(), equalTo( code ) );
    }

}
