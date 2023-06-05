package Persistence;

import Model.Book;
import Persistence.Exception.RepositoryException;

public interface IBookRepo extends IRepo<Book,Long>{
    Book findBookByTitleAndAuthor(Book book) throws RepositoryException;
}
