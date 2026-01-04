package org.hei.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final Connection connection;

    public DBConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new IllegalStateException("Variable non definie");
            }
            this.connection = DriverManager.getConnection(url, user, password);
        }

        public Connection getDBConnection(){
            return connection;
        }

        public void close() throws SQLException {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }