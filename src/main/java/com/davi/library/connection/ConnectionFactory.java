package com.davi.library.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection getConnection() throws SQLException {

        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3307/library_db");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv("DB_PASSWORD");

        return DriverManager.getConnection(url, user, password);


    }
}
