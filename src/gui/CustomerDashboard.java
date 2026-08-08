package gui;

import dao.BookingDAO;
import dao.CarDAO;
import dao.PaymentDAO;
import model.Car;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerDashboard extends JFrame {

    private final User user;

    private final CarDAO carDAO = new CarDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public CustomerDashboard(User user) {

        this.user = user;

        setTitle("Car Rental System - Customer");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel title = new JLabel(
                "CUSTOMER DASHBOARD",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(title, BorderLayout.NORTH);


        JPanel buttonPanel =
                new JPanel(new GridLayout(4, 2, 15, 15));

        JButton availableCars =
                new JButton("View Available Cars");

        JButton searchCars =
                new JButton("Search Cars");

        JButton bookCar =
                new JButton("Book Car");

        JButton myBookings =
                new JButton("My Bookings");

        JButton payment =
                new JButton("Make Payment");

        JButton viewPayment =
                new JButton("View Payment");

        JButton cancelBooking =
                new JButton("Cancel Booking");

        JButton logout =
                new JButton("Logout");


        buttonPanel.add(availableCars);
        buttonPanel.add(searchCars);
        buttonPanel.add(bookCar);
        buttonPanel.add(myBookings);
        buttonPanel.add(payment);
        buttonPanel.add(viewPayment);
        buttonPanel.add(cancelBooking);
        buttonPanel.add(logout);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);


        JLabel welcome =
                new JLabel(
                        "Logged in as: " + user.getUsername(),
                        SwingConstants.CENTER
                );

        mainPanel.add(welcome, BorderLayout.SOUTH);

        add(mainPanel);


        // BUTTON ACTIONS

        availableCars.addActionListener(
                e -> showCars(carDAO.getAvailableCars())
        );

        searchCars.addActionListener(
                e -> searchCars()
        );

        bookCar.addActionListener(
                e -> bookCar()
        );

        myBookings.addActionListener(
                e -> viewBookings()
        );

        payment.addActionListener(
                e -> makePayment()
        );

        viewPayment.addActionListener(
                e -> viewPayment()
        );

        cancelBooking.addActionListener(
                e -> cancelBooking()
        );

        logout.addActionListener(e -> {

            dispose();
            new LoginFrame();
        });


        setVisible(true);
    }


    // ================= VIEW CARS =================

    private void showCars(List<Car> cars) {

        String[] columns = {
                "ID",
                "Brand",
                "Model",
                "Category",
                "Year",
                "Price/Day",
                "Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };


        for (Car car : cars) {

            model.addRow(new Object[]{
                    car.getCarId(),
                    car.getBrandName(),
                    car.getModelName(),
                    car.getCategoryName(),
                    car.getManufacturingYear(),
                    "₹" + car.getPricePerDay(),
                    car.getStatus()
            });
        }


        JTable table = new JTable(model);

        table.setRowHeight(25);

        JScrollPane scrollPane =
                new JScrollPane(table);

        JDialog dialog =
                new JDialog(
                        this,
                        "Cars",
                        true
                );

        dialog.setSize(750, 400);
        dialog.setLocationRelativeTo(this);

        dialog.add(scrollPane);

        dialog.setVisible(true);
    }


    // ================= SEARCH =================

    private void searchCars() {

        String brand =
                JOptionPane.showInputDialog(
                        this,
                        "Enter brand name:"
                );

        if (brand == null || brand.isBlank()) {
            return;
        }

        List<Car> cars =
                carDAO.getCarsByBrand(brand);

        if (cars.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No cars found."
            );

            return;
        }

        showCars(cars);
    }


    // ================= BOOKING =================

    private void bookCar() {

        List<Car> cars =
                carDAO.getAvailableCars();

        if (cars.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No cars are currently available."
            );

            return;
        }

        showCars(cars);

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Car ID to book:"
                );

        if (input == null) {
            return;
        }

        try {

            int carId = Integer.parseInt(input);

            Car selectedCar =
                    carDAO.getCarById(carId);

            if (selectedCar == null ||
                    !selectedCar.getStatus()
                            .equalsIgnoreCase("AVAILABLE")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Car is not available."
                );

                return;
            }

            String start =
                    JOptionPane.showInputDialog(
                            this,
                            "Start date (YYYY-MM-DD):"
                    );

            String end =
                    JOptionPane.showInputDialog(
                            this,
                            "End date (YYYY-MM-DD):"
                    );

            if (start == null || end == null) {
                return;
            }

            java.sql.Date startDate =
                    java.sql.Date.valueOf(start);

            java.sql.Date endDate =
                    java.sql.Date.valueOf(end);

            long days =
                    (endDate.getTime() -
                            startDate.getTime())
                            / (1000 * 60 * 60 * 24);

            if (days <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "End date must be after start date."
                );

                return;
            }

            double total =
                    days * selectedCar.getPricePerDay();


            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Car: " +
                                    selectedCar.getBrandName()
                                    + " "
                                    + selectedCar.getModelName()
                                    + "\nDays: "
                                    + days
                                    + "\nTotal: ₹"
                                    + total
                                    + "\n\nConfirm booking?",
                            "Booking Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );


            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }


            model.Booking booking =
                    new model.Booking(
                            0,
                            user.getCustomerId(),
                            carId,
                            startDate,
                            endDate,
                            total,
                            "CONFIRMED"
                    );

            bookingDAO.bookCar(booking);

            JOptionPane.showMessageDialog(
                    this,
                    "Booking request completed."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input: " + e.getMessage()
            );
        }
    }


    // ================= BOOKINGS =================

    private void viewBookings() {

        JOptionPane.showMessageDialog(
                this,
                "Your booking history will use BookingDAO."
        );

        bookingDAO.viewBookingHistory(
                user.getCustomerId()
        );
    }


    // ================= PAYMENT =================

    // ================= PAYMENT =================

    private void makePayment() {

        String booking =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Booking ID:"
                );

        if (booking == null) {
            return;
        }

        String amount =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Amount:"
                );

        if (amount == null) {
            return;
        }

        String method =
                JOptionPane.showInputDialog(
                        this,
                        "Payment Method:"
                );

        if (method == null) {
            return;
        }

        try {

            int bookingId =
                    Integer.parseInt(booking);

            double paymentAmount =
                    Double.parseDouble(amount);

            paymentDAO.makePayment(
                    bookingId,
                    method,
                    paymentAmount
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid booking ID or amount."
            );
        }
    }


    // ================= VIEW PAYMENT =================

    private void viewPayment() {

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Payment ID:"
                );

        if (input == null) {
            return;
        }

        try {

            paymentDAO.viewPayment(
                    Integer.parseInt(input)
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid payment ID."
            );
        }
    }


    // ================= CANCEL =================

    private void cancelBooking() {

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Booking ID:"
                );

        if (input == null) {
            return;
        }

        try {

            int bookingId =
                    Integer.parseInt(input);

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Cancel booking "
                                    + bookingId
                                    + "?",
                            "Confirm",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirm ==
                    JOptionPane.YES_OPTION) {

                bookingDAO.cancelBooking(
                        bookingId
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid booking ID."
            );
        }
    }
}