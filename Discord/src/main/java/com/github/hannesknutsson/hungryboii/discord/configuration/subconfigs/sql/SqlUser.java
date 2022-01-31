package com.github.hannesknutsson.hungryboii.discord.configuration.subconfigs.sql;

import com.typesafe.config.Config;

public class SqlUser {

    public final String user;
    public final String pass;

    public SqlUser(Config userConfiguration) {
        this(userConfiguration.getString("username"), userConfiguration.getString("password"));
    }

    public SqlUser(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
}
