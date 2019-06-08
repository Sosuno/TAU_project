package REST.servlets;



import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.tau.sosuno.db.Book;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.DAO.BookDAO;


import java.util.List;

@Controller
public class BooksController {

    private BookDAO service = new BookDAO(ConnectDB.getCon());



    @RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Book> getAllBooks() {
        return service.getAll();
    }

    @RequestMapping(value = "/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Book getOneBook(@PathVariable("id") Long id) {
        return service.get(id).get();
    }

    @RequestMapping(value = "/books/{int}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Book> getSomeBooks(@PathVariable("int") int i) {
        return service.getSome(i);
    }




}
