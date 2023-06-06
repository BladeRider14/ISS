package Persistence;

import Model.Book;
import Model.Borrowed;
import Model.User;
import Persistence.Exception.RepositoryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookHibernateRepo implements IBookRepo {
    static SessionFactory sessionFactory;
    public BookHibernateRepo() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);

        }
    }

    @Override
    public Book findBookByTitleAndAuthor(Book book) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String queryString = "from Book as b where b.title like :title and b.author like :author";
                Book bookF =
                        session.createQuery(queryString, Book.class).setParameter("title", book.getTitle())
                                .setParameter("author",book.getAuthor())
                                .setMaxResults(1).uniqueResult();
                transaction.commit();
                if(bookF!=null){
                    return bookF;
                }
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        throw new RepositoryException("Nonexistent book");
    }

    @Override
    public void borrowBook(Borrowed borrowed) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(borrowed);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public void returnBook(Borrowed borrowed) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

//                Book crit = session.createQuery("from Message where text like 'New Hello%'", Message.class)
//                        .setMaxResults(1)
//                        .uniqueResult();
//                System.err.println("Stergem mesajul " + crit.getId());
                session.delete(borrowed);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public void add(Book book) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(book);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public void delete(Book book) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

//                Book crit = session.createQuery("from Message where text like 'New Hello%'", Message.class)
//                        .setMaxResults(1)
//                        .uniqueResult();
//                System.err.println("Stergem mesajul " + crit.getId());
                session.delete(book);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public void update(Book oldBook, Book newBook) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
//                String queryString = "from Book as b where b.id = :id";
//                Book book =
//                        session.createQuery(queryString, Book.class).setParameter("id", oldBook.getId())
//                                .setMaxResults(1).uniqueResult();
                newBook.setId(oldBook.getId());
                session.update(newBook);
                transaction.commit();

            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public Book findById(Long id) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String queryString = "from Book as b where b.id = :id";
                Book book =
                        session.createQuery(queryString, Book.class).setParameter("id", id)
                                .setMaxResults(1).uniqueResult();
                transaction.commit();
                if(book!=null){
                    return book;
                }
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        throw new RepositoryException("Nonexistent book");

    }

    @Override
    public Iterable<Book> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Book> books =
                        session.createQuery("from Book as b", Book.class)
                                .list();
                transaction.commit();
                return books;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;
    }

    @Override
    public Iterable<Borrowed> getAllBorrowedBooksByBook(Book book) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Borrowed> borrowed =
                        session.createQuery("from Borrowed as b where b.book=:book", Borrowed.class)
                                .setParameter("book",book)
                                .list();
                transaction.commit();
                return borrowed;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;

    }

    @Override
    public Iterable<Borrowed> getAllBorrowedBooks() {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Borrowed> borrowed =
                        session.createQuery("from Borrowed as b LEFT JOIN FETCH b.book left join fetch b.subscriber", Borrowed.class)
                                .list();

                List<Borrowed> finalBorrow= new ArrayList<>();
                borrowed.forEach(x->{
                    Borrowed b = session.load(Borrowed.class,x.getId());
                    finalBorrow.add(b);
                });
                transaction.commit();
                return finalBorrow;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;

    }
}
