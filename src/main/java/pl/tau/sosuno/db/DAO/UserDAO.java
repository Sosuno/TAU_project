package pl.tau.sosuno.db.DAO;

import pl.tau.sosuno.db.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {

    private Connection con;
    private PreparedStatement query;
    private ResultSet rs;

    public UserDAO(Connection c){
        con = c;


    }

    @Override
    public Optional<User> get(long id) {
        User user = null;
        try {
            query = con.prepareStatement("SELECT * FROM Users Where id = ?");

            query.setLong(1,id);
            rs = query.executeQuery();
            while (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("uuid"));

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return Optional.of(user);
    }

    @Override
    public List<User> getAll() {
        List users = new ArrayList();
        try {
            query = con.prepareStatement("SELECT * FROM Users");

            rs = query.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("uuid"));
                users.add(user);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getSome(int limit) {
        List users = new ArrayList();
        try {
            query = con.prepareStatement("SELECT * FROM Users LIMIT ?");
            query.setInt(1, limit);

            rs = query.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("email"));
                users.add(user);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Long save(User user) {
        User user1 = null;
        try {
            user1 = getBy("username", user.getUsername()).get();
        }catch(NullPointerException e) {
            e.printStackTrace();

        }


        try {
            if(user1 == null) {
                query = con.prepareStatement("INSERT INTO Users (username,password,email,uuid) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);

                query.setString(1, user.getUsername());
                query.setString(2, user.getPassword());
                query.setString(3, user.getEmail());
                query.setString(4, user.getUUID());
                query.executeUpdate();
                ResultSet generatedKeys = query.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }else {
                return -1l;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return user.getId();
    }


    @Override
    public User update(User user) {
        try {
            query = con.prepareStatement("UPDATE Users SET username = ?, password = ?, email = ?, UUID = ? WHERE id = ?");
            query.setString(1, user.getUsername());
            query.setString(2, user.getPassword());
            query.setString(3, user.getEmail());
            query.setString(4, user.getUUID());
            query.setLong(5, user.getId());
            query.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return get(user.getId()).get();
    }

    @Override
    public void delete(User user) {
        try {
            query = con.prepareStatement("DELETE FROM Users WHERE id = ?");
            query.setLong(1, user.getId());
            query.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<User> getBy(String column, String value) {
        User user = null;

        try {
            query = con.prepareStatement("SELECT * FROM Users Where " + column + " = ?");

            query.setString(1,value);
            rs = query.executeQuery();
            while (rs.next()) {

                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("uuid"));

            }


        }
        catch(Exception e){
            e.printStackTrace();
        }

        return Optional.of(user);
    }



}
