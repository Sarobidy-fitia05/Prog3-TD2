package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final int port = 5432;
    private final String host = System.getenv("DATABASE_HOST");
    private final String user = System.getenv("DATA_USER");
    private final String password = System.getenv("DATA_PASSWORD");
    private final String database = System.getenv("DATA_NAME");
    private final String jdbc_url;

    public DBConnection() {
        jdbc_url = "jdbc:postgresql://" + host +":"+ port +"/"+ database;
    }
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbc_url, user, password);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
