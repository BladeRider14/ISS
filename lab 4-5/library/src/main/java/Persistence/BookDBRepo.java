package Persistence;

import Model.Book;
import Persistence.Exception.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookDBRepo implements IBookRepo{

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public BookDBRepo(Properties properties) {
        logger.info("Initializing BookDBRepo with properties {}",properties);
        dbUtils = new JdbcUtils(properties);
    }


    @Override
    public void add(Book book) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("insert into books (title,author,exemplars) values (?,?,?)")){
            ps.setString(1,book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setLong(3,book.getExemplars());
            int result = ps.executeUpdate();
            logger.trace("Saved {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Book book) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("delete from books where id=?")){
            ps.setLong(1,book.getId());
            int result = ps.executeUpdate();
            logger.trace("Saved {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Book oldBook, Book newBook) {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("update books set title=?,author=?,exemplars=? where id=?")){
            ps.setString(1,newBook.getTitle());
            ps.setString(2,newBook.getAuthor());
            ps.setLong(3,newBook.getExemplars());
            ps.setLong(4,oldBook.getId());
            int result = ps.executeUpdate();
            logger.trace("Saved {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Book findById(Long idToFind) throws RepositoryException {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("select * from books where id=?")){
            ps.setLong(1,idToFind);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()){

                    return parseBook(rs);
                }
            }
        }catch (SQLException ex){
            logger.trace(ex);
        }
        throw new RepositoryException("Nonexistent Trip");
    }

    @Override
    public Iterable<Book> getAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<Book> books = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("select * from books")){
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    books.add(parseBook(rs));
                }
            }
        }catch (SQLException ex){
            logger.trace(ex);
        }
        return books;
    }

    private Book parseBook(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        Long exemplars = rs.getLong("exemplars");
        Book book = new Book(title,author);
        book.setId(id);
        book.setExemplars(exemplars);
        return book;
    }

    @Override
    public Book findBookByTitleAndAuthor(Book book) throws RepositoryException {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("select * from books where title=? and author=?")){
            ps.setString(1,book.getTitle());
            ps.setString(2,book.getAuthor());
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()){

                    return parseBook(rs);
                }
            }
        }catch (SQLException ex){
            logger.trace(ex);
        }
        throw new RepositoryException("Nonexistent Book");
    }
}
