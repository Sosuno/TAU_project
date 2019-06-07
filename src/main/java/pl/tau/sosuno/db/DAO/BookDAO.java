package pl.tau.sosuno.db.DAO;

import pl.tau.sosuno.db.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class BookDAO implements DAO<Book> {
    Connection connection = null;
    ResultSet rs = null;
    PreparedStatement query;

    public BookDAO(Connection con) {
        this.connection = con;
    }


    @Override
    public Optional<Book> get(long id) {
        Book book = null;
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Book Where id = ?");

            query.setLong(1,id);
            rs = query.executeQuery();
            while (rs.next()) {
                book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("authors"),
                        rs.getInt("year"), rs.getString("genres"), rs.getString("publisher"));

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return Optional.of(book);

    }

    @Override
    public List<Book> getAll() {
        return null;
    }

    @Override
    public void save(Book book) {

    }

    @Override
    public void update(Book book, String[] params) {

    }

    @Override
    public void delete(Book book) {

    }
}
