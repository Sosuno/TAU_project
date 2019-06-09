package pl.tau.sosuno;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    private ContextManger context;

    @Before
    public void setup() throws SQLException {
        Connection connection = ConnectDB.getCon();
        PreparedStatement query = connection.prepareStatement(
                "INSERT INTO USERS (username, password,email, uuid) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        expectedDbState = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            if(i%2 ==0) {
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


    @Test(expected = Exception.class)
    public void failLoginTest(){
        user = expectedDbState.get(1);
        user.setPassword("wrongPass");
        long id = context.login(user);
        assertNull(id);
    }

    @Test
    public void testLogin() {
        user = expectedDbState.get(1);
        long id = context.login(user);
        assertNotNull(id);
    }

    @Test
    public void testLogout(){
        user = expectedDbState.get(1);
        long id = context.logout(user);
        assertNull(id);
    }

    @Test
    public void testWrongUsernameLogin() {
        user = expectedDbState.get(1);
        long id = context.login(user);
        assertNull(id);
    }
}
