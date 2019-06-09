package pl.tau.sosuno.db.DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.tau.sosuno.db.Book;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.*;
public class UserDAOTest {

    private DAO userManager;

    List<User> expectedDbState;

    private long id = 1;
    private String username = "elpajaco";
    private String password = "myPass";
    private String email = "pajac@pajacowo.com";
    private String uuid = null;
    private String uuid2 = "123";
    private User user;

    @Before
    public void setup() throws SQLException {
        Connection connection = ConnectDB.getCon();
        PreparedStatement query = connection.prepareStatement(
                "INSERT INTO USERS (username, password,email, uuid) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        expectedDbState = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if(i%2 ==0) {
                user = new User(username +i, password, email, uuid);
            }
            else {
                user = new User(username +i, password, email, uuid2 + i);
            }

            try {
                query.setString(1, user.getUsername());
                query.setString(2, user.getPassword());
                query.setString(3, user.getEmail());
                query.setString(4, user.getUUID());
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
        userManager = new UserDAO(connection);
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
    public void testGet() {
        int count = 0;
        List<User> users = userManager.getAll();
        assertEquals(expectedDbState.size(),users.size());
        for (User user : users) {
            for(User user2 : expectedDbState){
                if(user.getId() == user2.getId()){
                    assertThat(user, samePropertyValuesAs(user2));
                    count++;
                }
            }
        }
        assertThat(count, equalTo(expectedDbState.size()));
        assertThat(count, equalTo(users.size()));
    }

    @Test
    public void testInsert() {
        user = new User(username,password,email,uuid);
        long key = userManager.save(user);
        User user2 = (User) userManager.get(key).get();
        assertThat(user2, samePropertyValuesAs(user));
    }

    @Test(expected = NullPointerException.class)
    public void testDelete() {
        assertNotNull(userManager.get(expectedDbState.get(1).getId()));
        User user = expectedDbState.get(1);
        userManager.delete(user);
        assertNull(userManager.get(expectedDbState.get(1).getId()));
    }

    @Test
    public void testUpdate() {
        User user = expectedDbState.get(1);
        user.setPassword("otherPass");
        userManager.update(user);
        User user1 = (User) userManager.get(user.getId()).get();
        assertThat(user, samePropertyValuesAs(user1));
       // assertNotEquals(user, user1);
    }

    @Test
    public void createUserWithExistingUsername() {
        user = new User(username+1,password,email,uuid2);
        assertThat(-1l, is(userManager.save(user)));
    }

    @Test
    public void getByUsernameTest() {
        User user2 = (User) userManager.getBy("username", expectedDbState.get(1).getUsername()).get();
        assertThat(expectedDbState.get(1), samePropertyValuesAs(user2));
    }

    @Test(expected = NullPointerException.class)
    public void getByNonExistingUsernameTest() {
        User user = (User) userManager.getBy("username", "blabla").get();
        assertNull(user);
    }

    @Test
    public void getUserByUUIDTest() {
        User user = (User) userManager.getBy("UUID",  expectedDbState.get(1).getUUID()).get();
        assertThat(expectedDbState.get(1), samePropertyValuesAs(user));
    }

    @Test(expected = NullPointerException.class)
    public void getUserByNullUUIDTest() {
        User user = (User) userManager.getBy("UUID", expectedDbState.get(0).getUUID()).get();
        assertNull(user);
    }


}
