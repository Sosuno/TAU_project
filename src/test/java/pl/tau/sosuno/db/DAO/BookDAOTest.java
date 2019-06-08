package pl.tau.sosuno.db.DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.tau.sosuno.db.Book;
import pl.tau.sosuno.db.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.*;

public class BookDAOTest {

    private DAO bookManager;

    List<Book> expectedDbState;

    private String[] authors = {"Neil Gaiman", "Terry Pratchett"};
    private String title = "Good Omens";
    private int year = 2010;
    private String[] genres = {"Comedy", "Supernatural"};
    private String publisher = "whoKnows";


    @Before
    public void setup() throws SQLException {
        Connection connection = ConnectDB.getCon();
        PreparedStatement addPersonStmt = connection.prepareStatement(
                "INSERT INTO Book (title, authors, year, genres, publisher) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        expectedDbState = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book book =  new Book(title+i, authors, year, genres, publisher);
            try {
                addPersonStmt.setString(1, book.getTitle());
                addPersonStmt.setString(2, book.getAuthorsToString());
                addPersonStmt.setInt(3, book.getYear());
                addPersonStmt.setString(4, book.getGenresToString());
                addPersonStmt.setString(5, book.getPublisher());
                addPersonStmt.executeUpdate();
                ResultSet generatedKeys = addPersonStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
            }

            expectedDbState.add(book);
        }
        bookManager = new BookDAO(connection);
    }

    @After
    public void cleanup() throws SQLException{
        Connection connection = ConnectDB.getCon();
        try {
            connection.prepareStatement("DELETE FROM Book").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGet() {
        Book book = (Book) bookManager.get(expectedDbState.get(1).getId()).get();
        assertEquals(expectedDbState.get(1).getTitle(), book.getTitle());
        assertArrayEquals(expectedDbState.get(1).getAuthors(), book.getAuthors());
        assertEquals(expectedDbState.get(1).getYear(),book.getYear());
        assertArrayEquals(expectedDbState.get(1).getGenres(), book.getGenres());
        assertEquals(expectedDbState.get(1).getPublisher(), book.getPublisher());
    }

    @Test
    public void getAll() {
        int count = 0;
        List<Book> books = bookManager.getAll();
        assertEquals(expectedDbState.size(),books.size());
        for (Book book : books) {
            for(Book book2 : expectedDbState){
                if(book.getId() == book2.getId()){
                    assertThat(book, samePropertyValuesAs(book2));
                    count++;
                }
            }
        }
        assertThat(count, equalTo(expectedDbState.size()));
        assertThat(count, equalTo(books.size()));
    }

    @Test
    public void testSave(){
        Book book = new Book(title, authors, year, genres, publisher);
        long key = bookManager.save(book);
        Book book1 = (Book) bookManager.get(key).get();
        assertThat(book1, samePropertyValuesAs(book));
    }

    @Test
    public void testUpdate(){
        Book book = expectedDbState.get(1);
        book.setYear(2014);
        bookManager.update(book);
        Book book1 = (Book) bookManager.get(book.getId()).get();
        assertThat(book, samePropertyValuesAs(book1));
        assertNotEquals(expectedDbState.get(1), book1);
    }

    @Test (expected = NullPointerException.class)
    public void testDelete(){
        assertNotNull(bookManager.get(expectedDbState.get(1).getId()));
        Book book = expectedDbState.get(1);
        bookManager.delete(book);
        assertNull(bookManager.get(expectedDbState.get(1).getId()));

    }


}
