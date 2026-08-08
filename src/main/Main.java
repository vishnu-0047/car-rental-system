package main;

import dao.BookingDAO;
import dao.CarDAO;
import dao.CustomerDAO;
import dao.PaymentDAO;
import model.Booking;
import model.Car;
import model.Customer;

import java.sql.Date;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static CarDAO carDAO = new CarDAO();
    static CustomerDAO customerDAO = new CustomerDAO();
    static BookingDAO bookingDAO = new BookingDAO();
    static PaymentDAO paymentDAO = new PaymentDAO();


    public static void main(String[] args) {

        while (true) {

            printMenu();

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    carDAO.viewAllCars();
                    break;

                case 2:
                    carDAO.viewAvailableCars();
                    break;

                case 3:
                    searchCars();
                    break;

                case 4:
                    addCustomer();
                    break;

                case 5:
                    customerDAO.viewAllCustomers();
                    break;

                case 6:
                    bookCar();
                    break;

                case 7:
                    viewBookings();
                    break;

                case 8:
                    makePayment();
                    break;

                case 9:
                    viewPayment();
                    break;

                case 10:
                    cancelBooking();
                    break;

                case 11:
                    updateCarStatus();
                    break;

                case 12:
                    deleteCar();
                    break;

                case 13:
                    System.out.println(
                            "Thank you for using Car Rental System."
                    );

                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


    // =========================================================
    // MENU
    // =========================================================

    public static void printMenu() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("          CAR RENTAL SYSTEM");
        System.out.println("==========================================");
        System.out.println("1.  View all cars");
        System.out.println("2.  View available cars");
        System.out.println("3.  Search cars by brand");
        System.out.println("4.  Add customer");
        System.out.println("5.  View all customers");
        System.out.println("6.  Book a car");
        System.out.println("7.  View booking history");
        System.out.println("8.  Make payment");
        System.out.println("9.  View payment");
        System.out.println("10. Cancel booking");
        System.out.println("11. Update car status");
        System.out.println("12. Delete car");
        System.out.println("13. Exit");
        System.out.println("==========================================");
    }


    // =========================================================
    // SEARCH CAR
    // =========================================================

    public static void searchCars() {

        System.out.print("Enter brand name: ");

        String brand = scanner.nextLine();

        carDAO.searchCarsByBrand(brand);
    }


    // =========================================================
    // ADD CUSTOMER
    // =========================================================

    public static void addCustomer() {

        System.out.println("\n--- Add Customer ---");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter license number: ");
        String license = scanner.nextLine();

        Customer customer = new Customer(
                0,
                name,
                phone,
                email,
                license
        );

        customerDAO.addCustomer(customer);
    }


    // =========================================================
    // BOOK CAR
    // =========================================================

    public static void bookCar() {

        System.out.println("\n--- Book Car ---");

        // -----------------------------------------------------
        // 1. Show available cars
        // -----------------------------------------------------

        System.out.println("\nAvailable Cars:");

        carDAO.viewAvailableCars();


        // -----------------------------------------------------
        // 2. Customer ID
        // -----------------------------------------------------

        System.out.print("\nEnter customer ID: ");

        int customerId = scanner.nextInt();
        scanner.nextLine();


        // -----------------------------------------------------
        // 3. Check customer
        // -----------------------------------------------------

        Customer customer =
                customerDAO.getCustomerById(customerId);

        if (customer == null) {

            System.out.println("Customer not found.");
            return;
        }

        System.out.println(
                "Customer: " + customer.getName()
        );


        // -----------------------------------------------------
        // 4. Car ID
        // -----------------------------------------------------

        System.out.print("Enter car ID: ");

        int carId = scanner.nextInt();
        scanner.nextLine();


        // -----------------------------------------------------
        // 5. Get selected car
        // -----------------------------------------------------

        Car selectedCar =
                carDAO.getCarById(carId);

        if (selectedCar == null) {

            System.out.println("Car not found.");
            return;
        }

        if (!selectedCar.getStatus()
                .equalsIgnoreCase("AVAILABLE")) {

            System.out.println(
                    "Car is not available."
            );

            return;
        }


        // -----------------------------------------------------
        // 6. Dates
        // -----------------------------------------------------

        System.out.print(
                "Enter start date (YYYY-MM-DD): "
        );

        String start = scanner.nextLine();

        System.out.print(
                "Enter end date (YYYY-MM-DD): "
        );

        String end = scanner.nextLine();


        Date startDate;
        Date endDate;

        try {

            startDate = Date.valueOf(start);
            endDate = Date.valueOf(end);

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid date format."
            );

            return;
        }


        // -----------------------------------------------------
        // 7. Calculate rental days
        // -----------------------------------------------------

        long days =
                (endDate.getTime() - startDate.getTime())
                        / (1000 * 60 * 60 * 24);

        if (days <= 0) {

            System.out.println(
                    "End date must be after start date."
            );

            return;
        }


        // -----------------------------------------------------
        // 8. Calculate total
        // -----------------------------------------------------

        double totalAmount =
                days * selectedCar.getPricePerDay();


        // -----------------------------------------------------
        // 9. Booking summary
        // -----------------------------------------------------

        System.out.println("\n--- Booking Summary ---");

        System.out.println(
                "Customer : " + customer.getName()
        );

        System.out.println(
                "Car      : " + selectedCar
        );

        System.out.println(
                "Days     : " + days
        );

        System.out.println(
                "Price/Day: ₹" +
                        selectedCar.getPricePerDay()
        );

        System.out.println(
                "Total    : ₹" + totalAmount
        );


        // -----------------------------------------------------
        // 10. Confirmation
        // -----------------------------------------------------

        System.out.print(
                "\nConfirm booking? (Y/N): "
        );

        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {

            System.out.println(
                    "Booking cancelled."
            );

            return;
        }


        // -----------------------------------------------------
        // 11. Create Booking object
        // -----------------------------------------------------

        Booking booking = new Booking(
                0,
                customerId,
                carId,
                startDate,
                endDate,
                totalAmount,
                "CONFIRMED"
        );


        // -----------------------------------------------------
        // 12. Send to DAO
        // -----------------------------------------------------

        bookingDAO.bookCar(booking);
    }


    // =========================================================
    // VIEW BOOKINGS
    // =========================================================

    public static void viewBookings() {

        System.out.print(
                "Enter customer ID: "
        );

        int customerId = scanner.nextInt();
        scanner.nextLine();

        bookingDAO.viewBookingHistory(customerId);
    }


    // =========================================================
    // MAKE PAYMENT
    // =========================================================

    public static void makePayment() {

        System.out.println("\n--- Make Payment ---");

        System.out.print(
                "Enter booking ID: "
        );

        int bookingId = scanner.nextInt();

        System.out.print(
                "Enter amount: "
        );

        double amount = scanner.nextDouble();

        scanner.nextLine();

        System.out.print(
                "Enter payment method: "
        );

        String method = scanner.nextLine();


        // PaymentDAO now directly handles the payment.
        paymentDAO.makePayment(
                bookingId,
                method,
                amount
        );
    }


    // =========================================================
    // VIEW PAYMENT
    // =========================================================

    public static void viewPayment() {

        System.out.print(
                "Enter booking ID: "
        );

        int bookingId = scanner.nextInt();
        scanner.nextLine();

        paymentDAO.viewPayment(bookingId);
    }


    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    public static void cancelBooking() {

        System.out.print(
                "Enter booking ID: "
        );

        int bookingId = scanner.nextInt();
        scanner.nextLine();

        bookingDAO.cancelBooking(bookingId);
    }


    // =========================================================
    // UPDATE CAR STATUS
    // =========================================================

    public static void updateCarStatus() {

        System.out.print(
                "Enter car ID: "
        );

        int carId = scanner.nextInt();

        scanner.nextLine();

        System.out.print(
                "Enter new status " +
                        "(AVAILABLE / RENTED / MAINTENANCE): "
        );

        String status = scanner.nextLine();

        carDAO.updateCarStatus(
                carId,
                status
        );
    }


    // =========================================================
    // DELETE CAR
    // =========================================================

    public static void deleteCar() {

        System.out.print(
                "Enter car ID: "
        );

        int carId = scanner.nextInt();

        scanner.nextLine();

        carDAO.deleteCar(carId);
    }
}