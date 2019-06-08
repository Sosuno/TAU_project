package pl.tau.sosuno.db;

public class User {

    private long id;
    private String username;
    private String password;
    private String email;
    private String UUID;

    public User() {

    }

    public User(String username, String password, String email, String uuid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.UUID = uuid;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(long id, String username, String password, String email, String UUID) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.UUID = UUID;
    }

    public User(long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String uuid) {
        this.UUID = uuid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
