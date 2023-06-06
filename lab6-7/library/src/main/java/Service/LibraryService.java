package Service;

import Model.Book;
import Model.Borrowed;
import Model.User;
import Persistence.Exception.RepositoryException;
import Persistence.IBookRepo;
import Persistence.IUserRepo;

import java.util.List;
import java.util.stream.StreamSupport;

public class LibraryService implements ILibraryService{

    private IUserRepo userRepo;
    private IBookRepo bookRepo;

    public LibraryService(IUserRepo userRepo, IBookRepo bookRepo) {
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public User checkLogin(User user) throws RepositoryException {
        try{
            User utf = userRepo.findUserByUsername(user.getUsername());
            if(utf.getPassword().equals(user.getPassword())){
                return utf;
            }
        }catch (RuntimeException exception){
            throw new RepositoryException("Mismatched username and password");
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
//        Iterable<Book> booksReal = bookRepo.getAll();
//        List<Book> booksList = StreamSupport.stream(booksReal.spliterator(),false).toList();
//        List<Book> books =List.copyOf(booksList);
//
//        if(books!=null){
//            books.forEach(book -> {
//                Long borrowed = StreamSupport.stream(bookRepo.getAllBorrowedBooksByBook(book).spliterator(), false)
//                        .count();
//                book.setExemplars(book.getExemplars());
//            });
//        }

        return bookRepo.getAll();
    }

    @Override
    public void borrowBook(Borrowed borrowed) {
        bookRepo.borrowBook(borrowed);
        Book borrowedBook = borrowed.getBook();
        borrowedBook.setExemplars(borrowedBook.getExemplars()-1);
        bookRepo.update(borrowedBook,borrowedBook);
    }

    @Override
    public void returnBook(Borrowed borrowed) {
        bookRepo.returnBook(borrowed);
        Book borrowedBook = borrowed.getBook();
        borrowedBook.setExemplars(borrowedBook.getExemplars()+1);
        bookRepo.update(borrowedBook,borrowedBook);
    }

    @Override
    public Iterable<Borrowed> getAllBorrowedBooks() {
        return bookRepo.getAllBorrowedBooks();
    }
}
