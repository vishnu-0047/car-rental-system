package dao;

import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    // =========================================================
    // 1. ADD CUSTOMER
    // =========================================================

    public void addCustomer(Customer customer) {

        String query = """
                INSERT INTO CUSTOMER
                (name, phone, email, license_number)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getLicenseNumber());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer added successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // =========================================================
    // 2. VIEW ALL CUSTOMERS
    // =========================================================

    public void viewAllCustomers() {

        String query = """
                SELECT customer_id,
                       name,
                       phone,
                       email,
                       license_number
                FROM CUSTOMER
                ORDER BY customer_id
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {

            System.out.println();
            System.out.println(
                    "--------------------------------------------------------------------------------"
            );

            System.out.printf(
                    "%-5s %-18s %-13s %-25s %-18s%n",
                    "ID",
                    "Name",
                    "Phone",
                    "Email",
                    "License"
            );

            System.out.println(
                    "--------------------------------------------------------------------------------"
            );

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.printf(
                        "%-5d %-18s %-13s %-25s %-18s%n",
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("license_number")
                );
            }

            if (!found) {
                System.out.println("No customers found.");
            }

            System.out.println(
                    "--------------------------------------------------------------------------------"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // =========================================================
    // 3. GET CUSTOMER BY ID
    // =========================================================

    public Customer getCustomerById(int customerId) {

        String query = """
                SELECT customer_id,
                       name,
                       phone,
                       email,
                       license_number
                FROM CUSTOMER
                WHERE customer_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("license_number")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}