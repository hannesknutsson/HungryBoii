package com.github.hannesknutsson.hungryboii.configuration.subconfigs.sql;

import com.github.hannesknutsson.hungryboii.configuration.MasterConfiguration;
import com.typesafe.config.Config;

public class SqlSettings {

    private static SqlSettings instance;

    private Config sqlConfig;

    private String ip;
    private int port;
    private String driver;
    private String databaseName;

    private SqlUser liquibaseUser;
    private SqlUser hibernateUser;

    private String hibernateDialectClass;
    private boolean hibernatePrintQueries;
    private SqlSettings() {
        sqlConfig = MasterConfiguration.getInstance().getSubConfiguration("sql");
        readEndpoint();
        readUsers();
        readHibernate();
    }

    public static SqlSettings getInstance() {
        if (instance == null) {
            instance = new SqlSettings();
        }
        return instance;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getDriver() {
        return driver;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getConnectionString() {
        SqlSettings sqlSettings = SqlSettings.getInstance();
        return sqlSettings.getDriver() + "://" + sqlSettings.getIp() + ":" + sqlSettings.getPort() + "/" + sqlSettings.getDatabaseName();
    }

    public SqlUser getLiquibaseUser() {
        return liquibaseUser;
    }

    public SqlUser getHibernateUser() {
        return hibernateUser;
    }

    public String getHibernateDialectClass() {
        return hibernateDialectClass;
    }

    public String isHibernatePrintQueries() {
        return ((Boolean) hibernatePrintQueries).toString();
    }

    private void readEndpoint() {
        Config serverConfig = sqlConfig.getConfig("server");
        this.ip = serverConfig.getString("ip");
        this.port = serverConfig.getInt("port");
        this.driver = serverConfig.getString("driver");
        this.databaseName = serverConfig.getString("databaseName");
    }

    private void readUsers() {
        Config userConfig = sqlConfig.getConfig("users");
        this.liquibaseUser = new SqlUser(userConfig.getConfig("liquibase"));
        this.hibernateUser = new SqlUser(userConfig.getConfig("hibernate"));
    }

    private void readHibernate() {
        Config hibernateConfig = sqlConfig.getConfig("hibernate");
        hibernateDialectClass = hibernateConfig.getString("dialectClass");
        hibernatePrintQueries = hibernateConfig.getBoolean("printQueries");
    }
}
