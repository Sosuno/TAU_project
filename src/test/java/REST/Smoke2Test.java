package REST;
import static org.assertj.core.api.Assertions.assertThat;


import REST.servlets.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Smoke2Test {

    @Autowired
    private UserController UserController;


    @Test
    public void contextLoads() throws Exception {
        assertThat(UserController).isNotNull();

    }
}
