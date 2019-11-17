package com.github.hannesknutsson.hungryboii.utilities.statichelpers.database.hibernate;

import com.github.hannesknutsson.hungryboii.configuration.subconfigs.sql.SqlSettings;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.DiscordUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class EntityCoupler {

    private static EntityCoupler instance;

    private final Configuration config;
    private final SessionFactory sessionFactory;

    private EntityCoupler() {
        config = new Configuration();
        setupConfig();
        addAnnotatedClasses();
        sessionFactory = config.buildSessionFactory();
    }

    public static EntityCoupler getInstance() {
        if (instance == null) {
            instance = new EntityCoupler();
        }
        return instance;
    }

    public static void initialize() {
        getInstance();
    }

    private void setupConfig() {
        SqlSettings sqlSettings = SqlSettings.getInstance();
        config.setProperty("hibernate.connection.url", sqlSettings.getConnectionString());
        config.setProperty("hibernate.connection.username", sqlSettings.getHibernateUser().user);
        config.setProperty("hibernate.connection.password", sqlSettings.getHibernateUser().pass);
        config.setProperty("hibernate.dialect", sqlSettings.getHibernateDialectClass());
        config.setProperty("hibernate.show_sql", sqlSettings.isHibernatePrintQueries());
    }

    private void addAnnotatedClasses() {
        config.addAnnotatedClass(DiscordUser.class);
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
