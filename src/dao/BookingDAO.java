package dao;

import model.Booking;
import util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDAO {

    // Book a car
    public void bookCar(Booking booking) {

        String checkCarQuery =
                "SELECT status FROM CAR WHERE car_id = ?";

        String insertBookingQuery = """
                INSERT INTO BOOKING
                (customer_id, car_id, start_date, end_date,
                 total_amount, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String updateCarQuery =
                "UPDATE CAR SET status = 'RENTED' WHERE car_id = ?";

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);
            // Check car availability
            try (PreparedStatement checkPS =
                         connection.prepareStatement(checkCarQuery)) {

                checkPS.setInt(1, booking.getCarId());

                try (ResultSet rs = checkPS.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Car not found.");
                        return;
                    }

                    String carStatus = rs.getString("status");

                    if (!carStatus.equalsIgnoreCase("AVAILABLE")) {
                        System.out.println(
                                "Car is not available."
                        );
                        return;
                    }
                }
            }


            // Insert booking
            try (PreparedStatement insertPS =
                         connection.prepareStatement(
                                 insertBookingQuery)) {

                insertPS.setInt(1, booking.getCustomerId());
                insertPS.setInt(2, booking.getCarId());
                insertPS.setDate(3, booking.getStartDate());
                insertPS.setDate(4, booking.getEndDate());
                insertPS.setDouble(5, booking.getTotalAmount());
                insertPS.setString(6, booking.getStatus());

                insertPS.executeUpdate();
            }


            // Change car status
            try (PreparedStatement updatePS =
                         connection.prepareStatement(updateCarQuery)) {

                updatePS.setInt(1, booking.getCarId());

                updatePS.executeUpdate();
            }

            connection.commit();

            System.out.println("Car booked successfully.");

        } catch (SQLException e) {

            if (connection != null) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            e.printStackTrace();

        } finally {

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // View booking history
    public void viewBookingHistory(int customerId) {

        String query = """
                SELECT
                    b.booking_id,
                    b.customer_id,
                    b.car_id,
                    m.model_name,
                    br.brand_name,
                    b.start_date,
                    b.end_date,
                    b.total_amount,
                    b.status
                FROM BOOKING b
                JOIN CAR c
                    ON b.car_id = c.car_id
                JOIN MODEL m
                    ON c.model_id = m.model_id
                JOIN BRAND br
                    ON m.brand_id = br.brand_id
                WHERE b.customer_id = ?
                ORDER BY b.booking_id
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {

                System.out.println();
                System.out.println(
                        "--------------------------------------------------------------------------"
                );

                System.out.printf(
                        "%-5s %-18s %-12s %-12s %-12s %-12s%n",
                        "ID",
                        "Car",
                        "Start",
                        "End",
                        "Amount",
                        "Status"
                );

                System.out.println(
                        "--------------------------------------------------------------------------"
                );

                while (rs.next()) {

                    String carName =
                            rs.getString("brand_name")
                                    + " "
                                    + rs.getString("model_name");

                    System.out.printf(
                            "%-5d %-18s %-12s %-12s ₹%-11.2f %-12s%n",
                            rs.getInt("booking_id"),
                            carName,
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getDouble("total_amount"),
                            rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Cancel booking
    public void cancelBooking(int bookingId) {

        String getCarQuery =
                "SELECT car_id FROM BOOKING WHERE booking_id = ?";

        String updateBookingQuery =
                "UPDATE BOOKING SET status = 'CANCELLED' " +
                        "WHERE booking_id = ?";

        String updateCarQuery =
                "UPDATE CAR SET status = 'AVAILABLE' " +
                        "WHERE car_id = ?";

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);
            int carId;

            // Find car
            try (PreparedStatement ps =
                         connection.prepareStatement(getCarQuery)) {

                ps.setInt(1, bookingId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Booking not found.");
                        return;
                    }

                    carId = rs.getInt("car_id");
                }
            }

            // Cancel booking
            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 updateBookingQuery)) {

                ps.setInt(1, bookingId);
                ps.executeUpdate();
            }

            // Make car available
            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 updateCarQuery)) {

                ps.setInt(1, carId);
                ps.executeUpdate();
            }

            connection.commit();

            System.out.println("Booking cancelled successfully.");

        } catch (SQLException e) {

            if (connection != null) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            e.printStackTrace();

        } finally {

            if (connection != null) {

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}