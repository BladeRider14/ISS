package Persistence;

import Model.User;
import Persistence.Exception.RepositoryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class UserHibernateRepo implements IUserRepo {
    static SessionFactory sessionFactory;
    public UserHibernateRepo() {
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

    public User add(User user) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return user;
    }

    @Override
    public User findUserByUsername(String username) throws RepositoryException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String queryString = "from User as u where u.username like :username";
                User user =
                        session.createQuery(queryString, User.class).setParameter("username", username)
                                .setMaxResults(1).uniqueResult();
                transaction.commit();
                return user;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        throw new RepositoryException("Nonexistent user");
    }
}
