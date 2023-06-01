package jm.task.core.jdbc.util;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static volatile Util instance;
public static Util getInstance() {
    Util localInstance = instance;
    if (localInstance == null) {
        synchronized (Util.class) {
            localInstance = instance;
            if (localInstance == null) {
                instance = localInstance = new Util();
            }
        }
    }
    return localInstance;
}
    private Util() {
    }
    private static final String PASSWORD = "root";
    private static final String USERNAME = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/users_data_base";

//Настройки соединения для Hibernate.
    public SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfigurationWithoutCfgXml();
        configuration.configure();

        return configuration.buildSessionFactory();
    }
// С использованием hibernate.cfg.xml
    private static Configuration BuildConfiguration() {
        var configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        return configuration;
    }
// Без hibernate.cfg.xml
    private static Configuration buildConfigurationWithoutCfgXml() {
        var configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.setProperty("connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", URL);
        configuration.setProperty("hibernate.connection.username", USERNAME);
        configuration.setProperty("hibernate.connection.password", PASSWORD);
        configuration.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.addAnnotatedClass(User.class);
        return configuration;
    }
// Настройки соединения для JDBC
    static {
        loadDriver();
    }
    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public Connection openConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
