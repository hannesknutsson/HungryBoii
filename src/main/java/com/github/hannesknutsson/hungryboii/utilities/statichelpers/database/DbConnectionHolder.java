package com.github.hannesknutsson.hungryboii.utilities.statichelpers.database;

import com.github.hannesknutsson.hungryboii.configuration.subconfigs.sql.SqlSettings;
import com.github.hannesknutsson.hungryboii.configuration.subconfigs.sql.SqlUser;
import liquibase.Liquibase;
import liquibase.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionHolder {

    private Connection liquibaseDbConnection;
    private Connection readAndWriteDbConnection;
    private Database database;
    private Liquibase liquibase;

    private static DbConnectionHolder instance;

    private DbConnectionHolder() {
    }

    public static DbConnectionHolder getInstance() {
        if (instance == null) {
            instance = new DbConnectionHolder();
        }
        return instance;
    }

    public Connection getLiquibaseDbConnection() {
        if (liquibaseDbConnection == null) {
            liquibaseDbConnection = openNewConnection(SqlSettings.getInstance().getLiquibaseUser());
        }
        return liquibaseDbConnection;
    }

    public Connection getReadAndWriteDbConnection() {
        if (readAndWriteDbConnection == null) {
            readAndWriteDbConnection = openNewConnection(SqlSettings.getInstance().getReadAndWriteUser());
        }
        return readAndWriteDbConnection;
    }

    private Connection openNewConnection(SqlUser user) {
        SqlSettings sqlSettings = SqlSettings.getInstance();
        Connection con;
        try {
            String connectionString = sqlSettings.getDriver() + "://" + sqlSettings.getIp() + ":" + sqlSettings.getPort() + "/" + sqlSettings.getDatabaseName();
            con = DriverManager.getConnection(
                    connectionString, user.user, user.pass);
        } catch (SQLException e) {
            e.printStackTrace();
            con = null;
        }
        return con;
    }
}
