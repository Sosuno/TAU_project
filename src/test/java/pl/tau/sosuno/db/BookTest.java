package pl.tau.sosuno.db;

import static org.junit.Assert.*;

import org.junit.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;


@RunWith(JUnit4.class)
public class BookTest {

    private long id = 123456;
    private String[] authors = {"Neil Gaiman", "Terry Pratchett"};
    private String title = "Good Omens";
    private int year = 2010;
    private String[] genres = {"Comedy", "Supernatural"};
    private String publisher = "whoKnows";
    private Book book;


    @Test
    public void createConnectionDbTest() {
        book = new Book();
        assertNotNull(book);
    }

    @Test
    public void BookSettersAndGettersTest() {
        book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthors(authors);
        book.setYear(year);
        book.setGenres(genres);
        book.setPublisher(publisher);

        assertEquals(id, book.getId());
        assertEquals(title, book.getTitle());
        assertArrayEquals(authors, book.getAuthors());
        assertEquals(year, book.getYear());
        assertArrayEquals(genres, book.getGenres());
        assertEquals(publisher, book.getPublisher());
    }

    @Test
    public void CreateBookWithConstructor() {
        book = new Book(id, title, authors, year, genres, publisher);
        assertNotNull(book);
        assertEquals(id, book.getId());
        assertEquals(title, book.getTitle());
        assertArrayEquals(authors, book.getAuthors());
        assertEquals(year, book.getYear());
        assertArrayEquals(genres, book.getGenres());
        assertEquals(publisher, book.getPublisher());
    }


}
