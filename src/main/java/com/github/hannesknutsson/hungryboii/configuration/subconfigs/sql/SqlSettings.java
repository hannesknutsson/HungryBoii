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

    private SqlUser readAndWriteUser;
    private SqlSettings() {
        sqlConfig = MasterConfiguration.getInstance().getSubConfiguration("sql");
        readEndpoint();
        readUsers();
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

    public SqlUser getLiquibaseUser() {
        return liquibaseUser;
    }

    public SqlUser getReadAndWriteUser() {
        return readAndWriteUser;
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
        this.readAndWriteUser = new SqlUser(userConfig.getConfig("readAndWrite"));
    }
}
