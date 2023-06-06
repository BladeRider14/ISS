package Persistence;

import Model.Book;
import Model.Borrowed;
import Persistence.Exception.RepositoryException;

public interface IBookRepo extends IRepo<Book,Long>{
    Book findBookByTitleAndAuthor(Book book) throws RepositoryException;
    void borrowBook(Borrowed borrowed);
    void returnBook(Borrowed borrowed);
    Iterable<Borrowed> getAllBorrowedBooks();
    Iterable<Borrowed> getAllBorrowedBooksByBook(Book book);
}
