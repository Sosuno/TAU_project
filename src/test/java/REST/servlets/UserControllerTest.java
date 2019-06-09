package REST.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.tau.sosuno.ContextManager;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private ContextManager context;

    private User user;
    List<User> expectedDbState;
    private long id = 1;
    private String username = "elpajaco";
    private String password = "myPass";
    private String email = "pajac@pajacowo.com";
    private String uuid = null;
    private String uuid2 = "123";


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
                query.setString(3, user.getEmail());
                query.setString(2, user.getPassword());
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
    public void loginTest() throws Exception{
        User userToLog = new User();
        userToLog.setUsername(expectedDbState.get(0).getUsername());
        userToLog.setPassword(expectedDbState.get(0).getPassword());

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(userToLog)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("email", is(expectedDbState.get(0).getEmail())))
                .andExpect(jsonPath("username", is(expectedDbState.get(0).getUsername())))
                .andExpect(jsonPath("password", is(expectedDbState.get(0).getPassword())))
                ;
    }

    @Test
    public void wrongUsernameLoginTest() throws Exception{
        User userToLog = new User();
        userToLog.setUsername("blbla");
        userToLog.setPassword(expectedDbState.get(0).getPassword());

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(userToLog)))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void wrongPasswordLoginTest() throws Exception{
        User userToLog = new User();
        userToLog.setUsername(expectedDbState.get(0).getUsername());
        userToLog.setPassword("wrongPass");

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(userToLog)))
                .andDo(print()).andExpect(status().isUnauthorized())
               ;
    }

    @Test
    public void changePasswordTest() throws Exception{
        User userToLog = new User();
        userToLog.setUsername(expectedDbState.get(0).getUsername());
        userToLog.setPassword("changedPass");

        this.mockMvc.perform(post("/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(userToLog)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("email", is(expectedDbState.get(0).getEmail())))
                .andExpect(jsonPath("username", is(expectedDbState.get(0).getUsername())))
                .andExpect(jsonPath("password", is(expectedDbState.get(0).getPassword())));
    }

    @Test
    public void logoutTest() throws Exception{
        User userToLog = new User();
        userToLog.setUsername(expectedDbState.get(0).getUsername());
        userToLog.setPassword("changedPass");

        this.mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(userToLog)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void register() throws Exception{
        User userToCreate = new User();
        userToCreate.setUsername("newUser");
        userToCreate.setPassword("newPass");
        userToCreate.setEmail("newMail@mailing.com");

        this.mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(userToCreate)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("username", is("newUser")))
                .andExpect(jsonPath("password", is("newPass")))
                .andExpect(jsonPath("email", is("newMail@mailing.com")));

    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            System.out.println(jsonContent);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
