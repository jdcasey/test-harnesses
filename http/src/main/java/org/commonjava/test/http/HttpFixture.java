package org.commonjava.test.http;

import java.util.Random;

import org.junit.rules.ExternalResource;

public class HttpFixture
    extends ExternalResource
{

    private static final Random RANDOM = new Random();

    private final short tries;

    private final int port = -1;

    @Override
    protected void before()
        throws Throwable
    {
        super.before();

        // stay above port 1024 for valid ports, or else we may need root
        // to open the port.
        int p;
        while ( port < 1024 )
        {
            p = RANDOM.nextInt();

        }
    }

    @Override
    protected void after()
    {
        // TODO Auto-generated method stub
        super.after();
    }

}
