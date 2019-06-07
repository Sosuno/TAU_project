package pl.tau.sosuno.db.DAO;

import static org.junit.Assert.*;

import org.junit.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.tau.sosuno.db.ConnectDB;

import java.sql.SQLException;


@RunWith(JUnit4.class)
public class BookDaoTest {


    String[] authors = {"Neil Gaiman", "Terry Pratchett"};
    String title = "Good Omens";
    int year = 2010;
    String[] genre = {"Comedy", "Supernatural"};
    String publisher = "whoKnows";
    BookDao book;

    @Before
    public void setup() throws SQLException {
        ConnectDB con = new ConnectDB();

    }

    @Test
    public void createConnectionDbTest() {
        book = new BookDao();
        assertNotNull(book);
    }

    @Test
    public void CreateBookWithConstructor() {

        BookDao book2 = new BookDao(title, authors, year, genre, publisher);
        assertNotNull(book2);
    }

}
