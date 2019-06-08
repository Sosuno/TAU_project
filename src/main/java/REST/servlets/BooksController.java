package REST.servlets;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.tau.sosuno.db.ConnectDB;
import pl.tau.sosuno.db.DAO.BookDAO;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Controller
public class BooksController {

    private BookDAO service = new BookDAO(ConnectDB.getCon());



    @RequestMapping("/books")
    @Produces("application/json")
    public @ResponseBody Response getAllBooks() {
        return Response.ok(service.getAll()).build();
    }
    @RequestMapping("/book/{id}")
    @Produces("application/json")
    public @ResponseBody Response getOneBook(@PathVariable("id") Long id) {
        return Response.ok(service.get(id)).build();
    }
    @RequestMapping("/books/{int}")
    @Produces("application/json")
    public @ResponseBody Response getSomeBooks(@PathVariable("int") int i) {
        return Response.ok(service.getSome(i)).build();
    }




}
