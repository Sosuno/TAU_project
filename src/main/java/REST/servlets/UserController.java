package REST.servlets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.tau.sosuno.ContextManager;
import pl.tau.sosuno.db.User;

import java.util.List;

@CrossOrigin(origins = "*")
@Controller
public class UserController {

    private ContextManager context = new ContextManager();


    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public User login(@RequestBody User user) {
        /*
        User u = new User();
        u.setPassword(pass);
        u.setUsername(username);*/
        user = context.login(user);
        System.out.println(user.toString());
        System.out.println(user.getId());
        if(user.getId() < 0){
            throw new UserNotFoundException();
        }else {
            return user;
        }
    }

    @RequestMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public String logout(@RequestBody User user){
        long response = context.logout(user);
        if(response < 0){
            throw new UserNotFoundException();
        }else {
            return "ok";
        }
    }

    @RequestMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public User update(@RequestBody User user) {
        user = context.changeUser(user);
        if(user.getId() < 0){
            throw new UserNotFoundException();
        }else {
            return user;
        }
    }

    @RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public User register(@RequestBody User user) {
        User newUser = context.registerUser(user);
        if (newUser.getId() == -2L) {
            throw new EmptyException();
        }
        if (newUser.getId() == -3L){
            throw new UsernameFoundException();
        }
        return user;

    }

    @ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="No such user")  // 401
    public class UserNotFoundException extends RuntimeException {
        // ...
    }

    @ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE, reason="Can't register")  // 406
    public class UsernameFoundException extends RuntimeException {
        // ...
    }
    @ResponseStatus(value=HttpStatus.I_AM_A_TEAPOT, reason="Empty fields")  // 418
    public class EmptyException extends RuntimeException {
        // ...
    }

}
