package pl.tau.sosuno.db.DAO;

public class BookDao {
    private String title;
    private String[] authors;
    private int year;
    private String[] genres;
    private String publisher;

    public BookDao() {
    }

    public BookDao(String title, String[] authors, int year, String[] genres, String publisher) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.genres = genres;
        this.publisher = publisher;
    }
}
