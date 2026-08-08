package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDAO {

    // =========================================================
    // 1. MAKE PAYMENT
    // =========================================================

    public void makePayment(
            int bookingId,
            String paymentMethod,
            double amount) {

        String checkBookingQuery = """
                SELECT booking_id,
                       total_amount,
                       status
                FROM BOOKING
                WHERE booking_id = ?
                """;

        String checkPaymentQuery = """
                SELECT payment_id
                FROM PAYMENT
                WHERE booking_id = ?
                AND payment_status = 'PAID'
                """;

        String insertPaymentQuery = """
                INSERT INTO PAYMENT
                (booking_id,
                 payment_method,
                 payment_date,
                 amount,
                 payment_status)
                VALUES (?, ?, SYSDATE, ?, 'PAID')
                """;

        try (
                Connection connection = DBConnection.getConnection()
        ) {

            // -------------------------------------------------
            // Check booking
            // -------------------------------------------------

            double bookingAmount;
            String bookingStatus;

            try (
                    PreparedStatement ps =
                            connection.prepareStatement(
                                    checkBookingQuery)
            ) {

                ps.setInt(1, bookingId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Booking not found.");
                        return;
                    }

                    bookingAmount =
                            rs.getDouble("total_amount");

                    bookingStatus =
                            rs.getString("status");
                }
            }


            // -------------------------------------------------
            // Check booking status
            // -------------------------------------------------

            if (bookingStatus.equalsIgnoreCase("CANCELLED")) {

                System.out.println(
                        "Cannot make payment for a cancelled booking."
                );

                return;
            }


            // -------------------------------------------------
            // Check whether already paid
            // -------------------------------------------------

            try (
                    PreparedStatement ps =
                            connection.prepareStatement(
                                    checkPaymentQuery)
            ) {

                ps.setInt(1, bookingId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        System.out.println(
                                "Payment already exists for this booking."
                        );

                        return;
                    }
                }
            }


            // -------------------------------------------------
            // Check amount
            // -------------------------------------------------

            if (Math.abs(amount - bookingAmount) > 0.01) {

                System.out.println(
                        "Incorrect payment amount."
                );

                System.out.println(
                        "Expected amount: ₹" + bookingAmount
                );

                return;
            }


            // -------------------------------------------------
            // Insert payment
            // -------------------------------------------------

            try (
                    PreparedStatement ps =
                            connection.prepareStatement(
                                    insertPaymentQuery)
            ) {

                ps.setInt(1, bookingId);
                ps.setString(2, paymentMethod);
                ps.setDouble(3, amount);

                ps.executeUpdate();
            }

            System.out.println("Payment successful.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // =========================================================
    // 2. VIEW PAYMENT
    // =========================================================

    public void viewPayment(int bookingId) {

        String query = """
                SELECT
                    p.payment_id,
                    p.booking_id,
                    c.name AS customer_name,
                    br.brand_name,
                    m.model_name,
                    p.payment_method,
                    p.payment_date,
                    p.amount,
                    p.payment_status
                FROM PAYMENT p
                JOIN BOOKING b
                    ON p.booking_id = b.booking_id
                JOIN CUSTOMER c
                    ON b.customer_id = c.customer_id
                JOIN CAR car
                    ON b.car_id = car.car_id
                JOIN MODEL m
                    ON car.model_id = m.model_id
                JOIN BRAND br
                    ON m.brand_id = br.brand_id
                WHERE p.booking_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println(
                            "No payment found for this booking."
                    );
                    return;
                }

                System.out.println();
                System.out.println(
                        "---------------------------------------------"
                );

                System.out.println(
                        "Payment ID      : " +
                                rs.getInt("payment_id")
                );

                System.out.println(
                        "Booking ID      : " +
                                rs.getInt("booking_id")
                );

                System.out.println(
                        "Customer        : " +
                                rs.getString("customer_name")
                );

                System.out.println(
                        "Car             : " +
                                rs.getString("brand_name") +
                                " " +
                                rs.getString("model_name")
                );

                System.out.println(
                        "Payment Method  : " +
                                rs.getString("payment_method")
                );

                System.out.println(
                        "Payment Date    : " +
                                rs.getDate("payment_date")
                );

                System.out.println(
                        "Amount          : ₹" +
                                rs.getDouble("amount")
                );

                System.out.println(
                        "Payment Status  : " +
                                rs.getString("payment_status")
                );

                System.out.println(
                        "---------------------------------------------"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}