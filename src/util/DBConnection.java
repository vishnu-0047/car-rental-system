package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XE";
    private static final String USERNAME = "your-username";
    private static final String PASSWORD = "your-pass";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
        System.out.println("Connected to Oracle database!");
        return connection;
    }
}
