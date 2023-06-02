package jm.task.core.jdbc.util;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

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

    public SessionFactory buildSessionFactory() {
        Configuration configuration = BuildConfiguration();

        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(ssrb.build());
    }
    private static Configuration BuildConfiguration() {
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
}
