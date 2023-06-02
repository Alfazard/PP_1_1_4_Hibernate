package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final Util instance = Util.getInstance();
    Session session = null;

    public UserDaoHibernateImpl() {
    }

    private void executeSql(String sql) {
        try (var sessionFactory = instance.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users
                (
                    id        SERIAL   PRIMARY KEY,
                    name      VARCHAR(64) NOT NULL,
                    last_name VARCHAR(64) NOT NULL,
                    age       TINYINT     NOT NULL
                );
                """;
        executeSql(sql);
    }

    @Override
    public void dropUsersTable() {
        String sql = """
                DROP TABLE IF EXISTS users;
                """;
        executeSql(sql);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (var sessionFactory = instance.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (var sessionFactory = instance.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "FROM User";
        try (var sessionFactory = instance.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            return session.createQuery(sql, User.class).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void cleanUsersTable() {
        String sql = """
                TRUNCATE users;
                """;
        executeSql(sql);
    }
}
