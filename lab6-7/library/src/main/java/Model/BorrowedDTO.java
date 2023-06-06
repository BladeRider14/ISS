package Model;

public class BorrowedDTO {
    private Borrowed burrowed;
    private String subscriber;
    private String book;
    private String returnDate;

    public BorrowedDTO(Borrowed burrowed) {
        this.burrowed = burrowed;
        this.subscriber=burrowed.getSubscriber().getUsername();
        this.book=burrowed.getBook().getTitle();
        this.returnDate=burrowed.getReturnDate();
    }

    public Borrowed getBurrowed() {
        return burrowed;
    }

    public void setBurrowed(Borrowed burrowed) {
        this.burrowed = burrowed;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
