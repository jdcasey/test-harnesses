package org.commonjava.test.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import org.commonjava.util.logging.Logger;

public final class PortFinder
{

    private static final Random RANDOM = new Random();

    private static final int MAX_PORT = 65535;

    private static final int MIN_PORT = 1024;

    private static final int PORT_MODULO = MAX_PORT - MIN_PORT;

    private static final Logger logger = new Logger( PortFinder.class );

    private PortFinder()
    {
    }

    public static boolean verifyUnused( final InetAddress address, final int port )
    {
        boolean success = false;
        try
        {
            logger.debug( "Trying port: %d", port );
            new Socket( address, port );
            logger.debug( "Socket occupied: %d", port );
        }
        catch ( final IOException e )
        {
            logger.debug( "Port %d is unused.", port );
            success = true;
        }

        return success;
    }

    public static int findRandomOpenPort( final InetAddress address, final int tries )
    {
        int p;
        for ( int i = 0; i < tries; i++ )
        {
            p = ( Math.abs( RANDOM.nextInt() ) % PORT_MODULO ) + MIN_PORT;
            if ( verifyUnused( address, p ) )
            {
                return p;
            }
        }

        throw new RuntimeException( "Could not find open port with " + tries + " random attempts." );
    }

}
