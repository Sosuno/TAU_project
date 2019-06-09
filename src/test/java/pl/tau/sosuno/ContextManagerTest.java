package pl.tau.sosuno;


import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.*;

public class ContextManagerTest {

    private User user;
    List<User> expectedDbState;
    private long id = 1;
    private String username = "elpajaco";
    private String password = "myPass";
    private String email = "pajac@pajacowo.com";
    private String uuid = null;
    private String uuid2 = "123";
    private ContextManager context;

    @Before
    public void setup() throws SQLException {
        Connection connection = ConnectDB.getCon();
        PreparedStatement query = connection.prepareStatement(
                "INSERT INTO USERS (username, password,email, uuid) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        expectedDbState = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            if(i == 0) {
                user = new User(username, password, email, uuid);
            }
            else {
                user = new User(username +i, password, email, uuid2);
            }
            try {
                query.setString(4, user.getUUID());
                query.setString(1, user.getUsername());
                query.setString(2, user.getPassword());
                query.setString(3, user.getEmail());
                query.executeUpdate();
                ResultSet generatedKeys = query.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e.getStackTrace().toString());
            }

            expectedDbState.add(user);
            context = new ContextManager();
        }

    }

    @After
    public void cleanup() throws SQLException{
        Connection connection = ConnectDB.getCon();
        try {
            connection.prepareStatement("DELETE FROM USERS").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void failLoginTest(){
        user = expectedDbState.get(0);
        user.setPassword("wrongPass");
        User u = context.login(user);
        assertNotNull(u);
        assertThat(u.getId(), is(-2L));
    }

    @Test
    public void testLogin() {
        user = expectedDbState.get(0);
        User u = context.login(user);
        assertNotNull(u);
    }

    @Test
    public void testLogout(){
        user = expectedDbState.get(1);
        long id = context.logout(user);
        assertEquals(id, 0L);
        user = expectedDbState.get(0);
        id = context.logout(user);
        assertEquals(id, -2L);
    }

    @Test
    public void testWrongUsernameLogin() {
        user = expectedDbState.get(0);
        user.setUsername("blabla");
        User id = context.login(user);
        assertNotNull(id);
        assertThat(id.getId(), is(-1L));
    }
    @Test
    public void testChangePassword() {
        user = expectedDbState.get(1);
        user.setPassword("newPass");
        User u = context.changeUser(user);
        assertThat(u, samePropertyValuesAs(user));

    }

    @Test
    public void createNewUserTest(){
        User u = new User();
        u.setUsername("newUser");
        u.setPassword("newPass");
        u.setEmail("newMail@mailin.com");

        User newUser = context.registerUser(u);
    }
}
