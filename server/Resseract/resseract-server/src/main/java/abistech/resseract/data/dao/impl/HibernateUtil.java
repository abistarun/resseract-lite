
package abistech.resseract.data.dao.impl;

import abistech.resseract.data.dao.dbo.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {

    private static Properties properties;
    private static SessionFactory sessionFactory;

    public static void initialize(Properties properties) {
        HibernateUtil.properties = properties;
        sessionFactory = buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(DBData.class);
        configuration.addAnnotatedClass(DBDataAccess.class);
        configuration.addAnnotatedClass(DBColumn.class);
        configuration.addAnnotatedClass(DBDashboard.class);
        configuration.addAnnotatedClass(DBDashboardAccess.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
