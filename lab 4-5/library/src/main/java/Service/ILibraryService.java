package Service;

import Model.Book;
import Model.User;
import Persistence.Exception.RepositoryException;

public interface ILibraryService {
    User checkLogin(User user) throws RepositoryException;
    void addBook(Book book);
    void deleteBook(Book book);
    void updateBook(Book oldBook,Book newBook);
    Iterable<Book> getALlBooks();
}
