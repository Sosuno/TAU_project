package pl.tau.sosuno.db;


import static org.junit.Assert.*;

import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(JUnit4.class)
public class ConnectDBTest {
    private static final Logger LOGGER = Logger.getLogger(ConnectDBTest.class.getCanonicalName());

    @Rule
    public Timeout globalTimeout = new Timeout(1000);



    @Test
    public void createConnectionDbTest() {
        ConnectDB con = new ConnectDB();
        assertNotNull(con);
    }

    @Test
    public void ConnectionTest() {
        ConnectDB con = new ConnectDB();
        try {
            assertNotNull(con.getCon());
        }
        catch(Exception e) {
            LOGGER.log(Level.FINEST,"Failed connection");
        }
    }


}
