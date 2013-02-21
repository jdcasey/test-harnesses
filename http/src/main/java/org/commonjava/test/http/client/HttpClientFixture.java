package org.commonjava.test.http.client;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.rules.ExternalResource;

public class HttpClientFixture
    extends ExternalResource
{

    private HttpClient client;

    @Override
    protected void before()
        throws Throwable
    {
        super.before();

        client = new DefaultHttpClient();
    }

    @Override
    protected void after()
    {
        super.after();
        if ( client != null )
        {
            client.getConnectionManager()
                  .closeExpiredConnections();
        }
    }

    public HttpResponse get( final String host, final int port, final String path )
        throws ClientProtocolException, IOException
    {
        final String url = "http://" + host + ":" + port + path;

        final HttpGet get = new HttpGet( url );
        return client.execute( get );
    }

    public HttpResponse get( final InetAddress address, final int port, final String path )
        throws ClientProtocolException, IOException
    {
        return get( address.getHostAddress(), port, path );
    }

}
