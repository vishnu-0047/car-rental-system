package dao;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User login(String username, String password) {

        String query = """
                SELECT
                    user_id,
                    username,
                    password,
                    role,
                    customer_id
                FROM USER_ACCOUNT
                WHERE username = ?
                AND password = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getInt("customer_id")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}