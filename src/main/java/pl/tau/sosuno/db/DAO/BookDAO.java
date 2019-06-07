package pl.tau.sosuno.db.DAO;

import pl.tau.sosuno.db.Book;

import java.sql.*;
import java.util.ArrayList;
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
        List books = new ArrayList();
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Book");

            rs = query.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("authors"),
                        rs.getInt("year"), rs.getString("genres"), rs.getString("publisher"));
                books.add(book);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Long save(Book book) {
        try {
            PreparedStatement query = connection.prepareStatement("INSERT INTO Book (title, authors, year, genres, publisher) VALUES (?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS);

            query.setString(1, book.getTitle());
            query.setString(2, book.getAuthorsToString());
            query.setInt(3, book.getYear());
            query.setString(4, book.getGenresToString());
            query.setString(5, book.getPublisher());
            query.executeUpdate();
            ResultSet generatedKeys = query.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong(1));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return book.getId();
    }

    @Override
    public Book update(Book book) {

        try {
            PreparedStatement query = connection.prepareStatement("UPDATE Book SET title = ?, authors = ?, year = ?, genres = ?, publisher =? WHERE id = ?");
            query.setString(1, book.getTitle());
            query.setString(2, book.getAuthorsToString());
            query.setInt(3, book.getYear());
            query.setString(4, book.getGenresToString());
            query.setString(5, book.getPublisher());
            query.setLong(6, book.getId());
            query.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return get(book.getId()).get();
    }

    @Override
    public void delete(Book book) {
        try {
            PreparedStatement query = connection.prepareStatement("DELETE FROM BOOK WHERE id = ?");
            query.setLong(1, book.getId());
            query.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
