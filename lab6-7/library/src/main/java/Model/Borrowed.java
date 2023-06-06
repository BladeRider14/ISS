package Model;

public class Borrowed implements HasId<Long>{
    private Long id;

    private String borrowedDate;
    private String returnDate;
    private Book book;
    private User subscriber;

    public Borrowed() {
    }

    public Borrowed(String borrowedDate, String returnDate) {
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
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
