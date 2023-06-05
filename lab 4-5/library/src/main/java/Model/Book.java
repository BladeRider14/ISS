package Model;

public class Book implements HasId<Long>{
    private Long id;
    private String title;
    private String author;
    private Long exemplars;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getExemplars() {
        return exemplars;
    }

    public void setExemplars(Long exemplars) {
        this.exemplars = exemplars;
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }
}
