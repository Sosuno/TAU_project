package pl.tau.sosuno.db;
import static org.junit.Assert.*;

import org.junit.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;


@RunWith(JUnit4.class)
public class UserTest {
    private long id = 1;
    private String username = "elpajaco";
    private String password = "myPass";
    private String email = "pajac@pajacowo.com";
    private String uuid = "";
    private String uuid2 = "123";
    private User user;



    @Test
    public void createUserTest() {
        user = new User();
        assertNotNull(user);
    }

    @Test
    public void userSettersAndGettersTest() {
        user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setUUID(uuid2);

        assertEquals(id, user.getId());
        assertEquals(username,user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(uuid2, user.getUUID());
    }

    @Test
    public void CreateUserWithConstructor() {
        user = new User(id, username,password,email);
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(username,user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
    }


}
