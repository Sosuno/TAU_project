package pl.tau.sosuno.db;

public class Book {
    private long id;
    private String title;
    private String[] authors;
    private int year;
    private String[] genres;
    private String publisher;

    public Book() {
    }

    public Book(long id, String title, String[] authors, int year, String[] genres, String publisher) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.genres = genres;
        this.publisher = publisher;
    }
    public Book(String title, String[] authors, int year, String[] genres, String publisher) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.genres = genres;
        this.publisher = publisher;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthorsToString() {
        return arrToString(authors);

    }
    public String getGenresToString() {
        return arrToString(genres);
    }

    private String arrToString(String[] arr) {
        StringBuilder toString = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            toString.append(arr[i]);
            if(i != arr.length-1){
                toString.append(", ");
            }
        }
        return toString.toString();
    }

}
