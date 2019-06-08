package REST.servlets;



import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.tau.sosuno.db.Book;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.DAO.BookDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    List<Book> expectedDbState;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookDAO service;

    @Before
    public void setup() throws SQLException {
        String[] authors = {"Neil Gaiman", "Terry Pratchett"};
        String title = "Good Omens";
        int year = 2010;
        String[] genres = {"Comedy", "Supernatural"};
        String publisher = "whoKnows";
        Connection connection = ConnectDB.getCon();
        PreparedStatement addBook = connection.prepareStatement(
                "INSERT INTO Book (title, authors, year, genres, publisher) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        expectedDbState = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book book =  new Book(title+i, authors, year, genres, publisher);
            try {
                addBook.setString(5, book.getPublisher());
                addBook.setString(1, book.getTitle());
                addBook.setString(2, book.getAuthorsToString());
                addBook.setInt(3, book.getYear());
                addBook.setString(4, book.getGenresToString());
                addBook.executeUpdate();
                ResultSet generatedKeys = addBook.getGeneratedKeys();
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e.getStackTrace().toString());
            }

            expectedDbState.add(book);
        }
        service = new BookDAO(ConnectDB.getCon());
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

    public List<String> arrToList(String[] arr) {
        List<String> list = new ArrayList<>();

        for (String str : arr){
            list.add(str);
        }
        return list;
    }
    @Test
    public void shouldReturnBookList() throws Exception {
        List <Book> books = service.getAll();
        this.mockMvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id", is((int)books.get(0).getId())))
                .andExpect(jsonPath("$[0].title", is(books.get(0).getTitle())))
                .andExpect(jsonPath("$[0].authors", is(arrToList(books.get(0).getAuthors()))))
                .andExpect(jsonPath("$[0].year", is(books.get(0).getYear())))
                .andExpect(jsonPath("$[0].genres", is(arrToList(books.get(0).getGenres()))))
                .andExpect(jsonPath("$[0].publisher", is(books.get(0).getPublisher())));
    }

    @Test
    public void shouldReturnOneBook() throws Exception {
        Book book = expectedDbState.get(1);
        this.mockMvc.perform(get("/book/{id}", book.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id", is((int)book.getId())))
                .andExpect(jsonPath("title", is(book.getTitle())))
                .andExpect(jsonPath("authors", is(arrToList(book.getAuthors()))))
                .andExpect(jsonPath("year", is(book.getYear())))
                .andExpect(jsonPath("genres", is(arrToList(book.getGenres()))))
                .andExpect(jsonPath("publisher", is(book.getPublisher())));
    }

    @Test
    public void shouldReturnNoOfBooks() throws Exception {
        List <Book> books = service.getSome(5);
        this.mockMvc.perform(get("/books/{int}", 5)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is((int)books.get(0).getId())))
                .andExpect(jsonPath("$[0].title", is(books.get(0).getTitle())))
                .andExpect(jsonPath("$[0].authors", is(arrToList(books.get(0).getAuthors()))))
                .andExpect(jsonPath("$[0].year", is(books.get(0).getYear())))
                .andExpect(jsonPath("$[0].genres", is(arrToList(books.get(0).getGenres()))))
                .andExpect(jsonPath("$[0].publisher", is(books.get(0).getPublisher())));
    }

    @Test
    public void shouldReturn404() throws Exception {
        this.mockMvc.perform(get("/books/id")).andDo(print()).andExpect(status().isBadRequest());

    }
}
