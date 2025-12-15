package org.hei.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/mini_football_db";
    private static final String USER = "mini_football_db_manager";
    private static final String PASSWORD = "123456";

    public Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
