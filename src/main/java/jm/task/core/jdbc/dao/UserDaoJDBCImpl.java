package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Util instance = Util.getInstance();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users
                (
                    id       SERIAL PRIMARY KEY,
                    name     VARCHAR(64) NOT NULL,
                    lastName VARCHAR(64) NOT NULL,
                    age      TINYINT     NOT NULL
                );
                """;
        Connection connection = null;
        PreparedStatement prepareStatement;
        try {
            connection = instance.openConnection();
            prepareStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            prepareStatement.execute();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dropUsersTable() {
        String sql = """
                DROP TABLE IF EXISTS users;
                """;
        Connection connection = null;
        PreparedStatement prepareStatement;
        try {
            connection = instance.openConnection();
            prepareStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            prepareStatement.execute();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = """
                INSERT INTO users (name, lastName, age)
                VALUES (?, ?, ?)
                """;
        Connection connection = null;
        PreparedStatement prepareStatement;
        try {
            connection = instance.openConnection();
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, name);
            prepareStatement.setString(2, lastName);
            prepareStatement.setByte(3, age);
            connection.setAutoCommit(false);
            prepareStatement.executeUpdate();
            connection.commit();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE
                FROM users
                WHERE id = ?;
                """;
        Connection connection = null;
        PreparedStatement prepareStatement;
        try {
            connection = instance.openConnection();
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setLong(1, id);
            connection.setAutoCommit(false);
            prepareStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = """
                SELECT id,
                       name,
                       lastName,
                       age
                FROM users;
                """;
        Connection connection = null;
        PreparedStatement prepareStatement;
        try {
            connection = instance.openConnection();
            prepareStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return userList;

    }

    public void cleanUsersTable() {
        String sql = """
                TRUNCATE users;
                """;
        Connection connection = null;
        PreparedStatement prepareStatement;
        try {
            connection = instance.openConnection();
            prepareStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            prepareStatement.execute();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
