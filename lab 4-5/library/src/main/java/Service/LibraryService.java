package Service;

import Model.Book;
import Model.User;
import Persistence.Exception.RepositoryException;
import Persistence.IBookRepo;
import Persistence.IUserRepo;

public class LibraryService implements ILibraryService{

    private IUserRepo userRepo;
    private IBookRepo bookRepo;

    public LibraryService(IUserRepo userRepo, IBookRepo bookRepo) {
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public User checkLogin(User user) throws RepositoryException {
        User utf = userRepo.findUserByUsername(user.getUsername());
        if(utf.getPassword().equals(user.getPassword())){
            return utf;
        }
        throw new RepositoryException("Mismatched username and password");
    }

    @Override
    public void addBook(Book book) {
        try{
            Book btf = bookRepo.findBookByTitleAndAuthor(book);
            btf.setExemplars(btf.getExemplars()+book.getExemplars());
            bookRepo.update(btf,btf);
        }catch (RepositoryException re){
            bookRepo.add(book);
        }
    }

    @Override
    public void deleteBook(Book book) {
        bookRepo.delete(book);
    }

    @Override
    public void updateBook(Book oldBook, Book newBook) {
        bookRepo.update(oldBook,newBook);
    }

    @Override
    public Iterable<Book> getALlBooks() {
        return bookRepo.getAll();
    }
}
