package dao;

import model.Car;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    // Common query used to get complete car information
    private static final String BASE_QUERY = """
            SELECT
                c.car_id,
                c.model_id,
                c.registration_number,
                c.manufacturing_year,
                c.price_per_day,
                c.status,
                b.brand_name,
                m.model_name,
                cat.category_name
            FROM CAR c
            JOIN MODEL m
                ON c.model_id = m.model_id
            JOIN BRAND b
                ON m.brand_id = b.brand_id
            JOIN CATEGORY cat
                ON m.category_id = cat.category_id
            """;


    // 1. View all cars
    public void viewAllCars() {

        String query = BASE_QUERY + " ORDER BY c.car_id";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {

            printHeading();

            while (rs.next()) {

                Car car = createCarFromResultSet(rs);

                System.out.println(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 2. View available cars
    public void viewAvailableCars() {

        String query =
                BASE_QUERY +
                        " WHERE c.status = 'AVAILABLE' ORDER BY c.car_id";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {

            printHeading();

            while (rs.next()) {

                Car car = createCarFromResultSet(rs);

                System.out.println(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 3. Search cars by brand
    public void searchCarsByBrand(String brandName) {

        String query =
                BASE_QUERY +
                        " WHERE UPPER(b.brand_name) = UPPER(?) " +
                        " ORDER BY c.car_id";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setString(1, brandName);

            try (ResultSet rs = ps.executeQuery()) {

                printHeading();

                while (rs.next()) {

                    Car car = createCarFromResultSet(rs);

                    System.out.println(car);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 4. Add a car
    public void addCar(Car car) {

        String query = """
                INSERT INTO CAR
                (model_id,
                 registration_number,
                 manufacturing_year,
                 price_per_day,
                 status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setInt(1, car.getModelId());
            ps.setString(2, car.getRegistrationNumber());
            ps.setInt(3, car.getManufacturingYear());
            ps.setDouble(4, car.getPricePerDay());
            ps.setString(5, car.getStatus());

            int rows = ps.executeUpdate();

            System.out.println(rows + " car added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 5. Update car status
    public void updateCarStatus(int carId, String status) {

        String query =
                "UPDATE CAR SET status = ? WHERE car_id = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setString(1, status);
            ps.setInt(2, carId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Car status updated successfully.");
            } else {
                System.out.println("Car not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 6. Delete car
    public void deleteCar(int carId) {

        String query =
                "DELETE FROM CAR WHERE car_id = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setInt(1, carId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Car deleted successfully.");
            } else {
                System.out.println("Car not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Converts one database row into a Car object
    private Car createCarFromResultSet(ResultSet rs)
            throws SQLException {

        return new Car(
                rs.getInt("car_id"),
                rs.getInt("model_id"),
                rs.getString("registration_number"),
                rs.getInt("manufacturing_year"),
                rs.getDouble("price_per_day"),
                rs.getString("status"),
                rs.getString("brand_name"),
                rs.getString("model_name"),
                rs.getString("category_name")
        );
    }

    public Car getCarById(int carId) {

        String query =
                BASE_QUERY +
                        " WHERE c.car_id = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(query)
        ) {

            ps.setInt(1, carId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return createCarFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= GUI METHODS =================

    // Get all cars as a List
    public List<Car> getAllCars() {

        List<Car> cars = new ArrayList<>();

        String query = BASE_QUERY + " ORDER BY c.car_id";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                cars.add(createCarFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }


    // Get available cars as a List
    public List<Car> getAvailableCars() {

        List<Car> cars = new ArrayList<>();

        String query =
                BASE_QUERY +
                        " WHERE c.status = 'AVAILABLE' ORDER BY c.car_id";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                cars.add(createCarFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }


    // Search cars by brand
    public List<Car> getCarsByBrand(String brandName) {

        List<Car> cars = new ArrayList<>();

        String query =
                BASE_QUERY +
                        " WHERE UPPER(b.brand_name) = UPPER(?) " +
                        " ORDER BY c.car_id";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
        ) {

            ps.setString(1, brandName);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    cars.add(createCarFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    // Prints table heading
    private void printHeading() {

        System.out.println();
        System.out.println(
                "-----------------------------------------------------------------------------------------"
        );

        System.out.printf(
                "%-4s %-15s %-15s %-15s %-6s %-10s %-12s%n",
                "ID",
                "Brand",
                "Model",
                "Category",
                "Year",
                "Price/Day",
                "Status"
        );

        System.out.println(
                "-----------------------------------------------------------------------------------------"
        );
    }
}