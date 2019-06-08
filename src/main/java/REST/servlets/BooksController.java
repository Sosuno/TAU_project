package REST.servlets;



import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pl.tau.sosuno.db.Book;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.DAO.BookDAO;


import java.util.List;

@Controller
public class BooksController {

    private BookDAO service = new BookDAO(ConnectDB.getCon());



    @RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public List<Book> getAllBooks() {
        return service.getAll();
    }

    @RequestMapping(value = "/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public Book getOneBook(@PathVariable("id") Long id) {
        return service.get(id).get();
    }

    @RequestMapping(value = "/books/{int}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public List<Book> getSomeBooks(@PathVariable("int") int i) {
        return service.getSome(i);
    }
    @RequestMapping(value = "/book/{int}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String deleteBook(@PathVariable("int") Long i) {
        service.delete(service.get(i).get());
        return "deleted";
    }

    @RequestMapping(value = "/book/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@RequestBody Book book) {
        Long l = service.save(book);
        Book book2 = service.get(l).get();
        return book2;
    }

    @RequestMapping(value = "/book/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public Book addBook(@RequestBody Book book) {
        Book book2 = service.update(book);
        return book2;
    }





}
