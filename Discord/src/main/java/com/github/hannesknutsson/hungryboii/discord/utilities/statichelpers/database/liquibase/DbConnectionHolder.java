package com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.database.liquibase;

import com.github.hannesknutsson.hungryboii.discord.configuration.subconfigs.sql.SqlSettings;
import com.github.hannesknutsson.hungryboii.discord.configuration.subconfigs.sql.SqlUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionHolder {

    private Connection liquibaseDbConnection;

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

    private Connection openNewConnection(SqlUser user) {
        Connection con;
        try {
            con = DriverManager.getConnection(
                    SqlSettings.getInstance().getConnectionString(), user.user, user.pass);
        } catch (SQLException e) {
            e.printStackTrace();
            con = null;
        }
        return con;
    }
}
