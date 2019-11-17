package com.github.hannesknutsson.hungryboii.utilities.statichelpers.database.hibernate;

import com.github.hannesknutsson.hungryboii.configuration.subconfigs.sql.SqlSettings;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class EntityCoupler {

    private final Configuration config;
    private final SessionFactory sessionFactory;

    private EntityCoupler() {
        config = new Configuration();
        setupConfig();
        addAnnotatedClasses();
        sessionFactory = config.buildSessionFactory();
    }

    private void setupConfig() {
        SqlSettings sqlSettings = SqlSettings.getInstance();
        config.setProperty("hibernate.connection.driver_class", sqlSettings.getHibernateDriverClass());
        config.setProperty("hibernate.connection.url", sqlSettings.getConnectionString());
        config.setProperty("hibernate.connection.username", sqlSettings.getHibernateUser().user);
        config.setProperty("hibernate.connection.password", sqlSettings.getHibernateUser().pass);
        config.setProperty("hibernate.dialect", sqlSettings.getHibernateDialectClass());
    }

    private void addAnnotatedClasses() {
        //config.addAnnotatedClass(Asda.class);
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
