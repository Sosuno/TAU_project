package pl.tau.sosuno;

import pl.tau.sosuno.db.Book;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.DAO.BookDAO;
import pl.tau.sosuno.db.DAO.DAO;
import pl.tau.sosuno.db.DAO.UserDAO;
import pl.tau.sosuno.db.User;

import java.util.UUID;

public class ContextManager {

    private DAO userManager;
    private DAO bookManager;

    public ContextManager() {
        userManager = new UserDAO(ConnectDB.getCon());
        bookManager = new BookDAO(ConnectDB.getCon());
    }


    public User login(User user) {
        User u = (User) userManager.getBy("username",user.getUsername()).get();
        if(u.getId() == -1L) {
            return u;
        }
        else if(u.getPassword().equals(user.getPassword())) {
            UUID uuid = UUID.randomUUID();
            u.setUUID(uuid.toString());
            user = (User) userManager.update(u);
            return user;
        }else {
            u = new User();
            u.setId(-2l);
            return u;
        }
    }

    public Long logout(User user) {
        if(user.getUUID() == null) return -2L;
        User u = (User) userManager.getBy("uuid",user.getUUID()).get();
        u.setUUID(null);
        user = (User) userManager.update(u);
        if(user.getUUID() == null) return 0L;
        else return -1L;
    }
    public User changeUser(User user) {
        User u = (User) userManager.getBy("uuid",user.getUUID()).get();
        u.setPassword(user.getPassword());
        u.setEmail(user.getEmail());
        u.setUsername(user.getUsername());
        user = (User) userManager.update(u);
        return user;
    }

    public Book addBook() {return null;}

    public Book changeBook() {
            return null;
    }






}
