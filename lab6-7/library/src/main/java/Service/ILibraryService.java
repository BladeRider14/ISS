package Service;

import Model.Book;
import Model.Borrowed;
import Model.User;
import Persistence.Exception.RepositoryException;

public interface ILibraryService {
    User checkLogin(User user) throws RepositoryException;
    void addBook(Book book);
    void deleteBook(Book book);
    void updateBook(Book oldBook,Book newBook);
    Iterable<Book> getALlBooks();

    void borrowBook(Borrowed borrowed);
    void returnBook(Borrowed borrowed);
    Iterable<Borrowed> getAllBorrowedBooks();
}
