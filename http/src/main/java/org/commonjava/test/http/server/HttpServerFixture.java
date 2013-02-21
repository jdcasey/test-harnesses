package org.commonjava.test.http.server;

import static org.commonjava.test.util.PortFinder.findRandomOpenPort;
import static org.commonjava.test.util.PortFinder.verifyUnused;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.commonjava.test.http.server.impl.RequestHandler;
import org.commonjava.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.junit.rules.ExternalResource;

public class HttpServerFixture
    extends ExternalResource
{

    private final Logger logger = new Logger( getClass() );

    private final InetAddress localAddress;

    private final short portFinderTries;

    private int port;

    private Server server;

    private final Map<String, Content> contentMap = new HashMap<String, Content>();

    public HttpServerFixture()
    {
        portFinderTries = 5;
        port = -1;
        localAddress = getLocalAddress();
    }

    public HttpServerFixture( final short portFinderTries )
    {
        this.portFinderTries = portFinderTries;
        port = -1;
        localAddress = getLocalAddress();
    }

    public HttpServerFixture( final int port )
    {
        this.port = port;
        this.portFinderTries = 0;
        localAddress = getLocalAddress();
    }

    public void addContent( final String path, final Content content )
    {
        contentMap.put( path, content );
    }

    public void addContent( final Map<String, Content> map )
    {
        contentMap.putAll( map );
    }

    public void clearContent()
    {
        contentMap.clear();
    }

    public void removeContent( final String path )
    {
        contentMap.remove( path );
    }

    public void removeContent( final String... paths )
    {
        for ( final String path : paths )
        {
            removeContent( path );
        }
    }

    public void removeContent( final Collection<String> paths )
    {
        for ( final String path : paths )
        {
            removeContent( path );
        }
    }

    public Content getContent( final String path )
    {
        return contentMap.get( path );
    }

    public Map<String, Content> getContent()
    {
        return contentMap;
    }

    public SocketAddress getSocketAddress()
        throws UnknownHostException
    {
        return new InetSocketAddress( localAddress, port );
    }

    @Override
    protected void before()
        throws Throwable
    {
        super.before();

        // stay above port 1024 for valid ports, or else we may need root
        // to open the port.
        if ( port < 0 )
        {
            port = findRandomOpenPort( localAddress, portFinderTries );
        }
        else
        {
            if ( !verifyUnused( localAddress, port ) )
            {
                throw new RuntimeException( "Port is already in use: " + port );
            }
        }

        startServer();
    }

    protected void startServer()
        throws Throwable
    {
        logger.debug( "Starting HTTP server on port: %s", getPort() );
        server = new Server( port );
        server.setHandler( new RequestHandler( contentMap ) );
        server.start();

        logger.debug( "HTTP server started on: %s", getSocketAddress() );
    }

    public int getPort()
    {
        return port;
    }

    public InetAddress getAddress()
    {
        return localAddress;
    }

    @Override
    protected void after()
    {
        super.after();
        logger.debug( "Attempting to shutdown: %s", server );
        if ( server != null )
        {
            try
            {
                server.stop();
            }
            catch ( final Exception e )
            {
                throw new RuntimeException( "Failed to stop server.", e );
            }

            try
            {
                server.join();
            }
            catch ( final InterruptedException e )
            {
            }
        }
    }

    private InetAddress getLocalAddress()
    {
        try
        {
            return InetAddress.getByName( "127.0.0.1" );
        }
        catch ( final UnknownHostException e )
        {
            throw new RuntimeException( "Cannot lookup 127.0.0.1: " + e.getMessage(), e );
        }
    }

}
