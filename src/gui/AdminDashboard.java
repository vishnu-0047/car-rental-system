package gui;

import dao.BookingDAO;
import dao.CarDAO;
import dao.CustomerDAO;
import dao.PaymentDAO;
import model.Car;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final User user;

    private final CarDAO carDAO = new CarDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public AdminDashboard(User user) {

        this.user = user;

        setTitle("Car Rental System - Admin");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        // ================= TITLE =================

        JLabel title =
                new JLabel(
                        "ADMIN DASHBOARD",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(title, BorderLayout.NORTH);


        // ================= BUTTONS =================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(4, 2, 15, 15)
                );

        JButton viewCars =
                new JButton("View All Cars");

        JButton addCar =
                new JButton("Add Car");

        JButton updateCar =
                new JButton("Update Car Status");

        JButton deleteCar =
                new JButton("Delete Car");

        JButton viewCustomers =
                new JButton("View Customers");

        JButton viewBookings =
                new JButton("View Bookings");

        JButton viewPayments =
                new JButton("View Payments");

        JButton logout =
                new JButton("Logout");


        buttonPanel.add(viewCars);
        buttonPanel.add(addCar);
        buttonPanel.add(updateCar);
        buttonPanel.add(deleteCar);
        buttonPanel.add(viewCustomers);
        buttonPanel.add(viewBookings);
        buttonPanel.add(viewPayments);
        buttonPanel.add(logout);

        mainPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );


        // ================= WELCOME =================

        JLabel welcome =
                new JLabel(
                        "Logged in as: "
                                + user.getUsername(),
                        SwingConstants.CENTER
                );

        mainPanel.add(
                welcome,
                BorderLayout.SOUTH
        );

        add(mainPanel);


        // =================================================
        // VIEW ALL CARS
        // =================================================

        viewCars.addActionListener(e -> {

            List<Car> cars =
                    carDAO.getAllCars();

            showCars(cars);
        });


        // =================================================
        // ADD CAR
        // =================================================

        addCar.addActionListener(e -> {

            addCar();
        });


        // =================================================
        // UPDATE CAR STATUS
        // =================================================

        updateCar.addActionListener(e -> {

            updateCarStatus();
        });


        // =================================================
        // DELETE CAR
        // =================================================

        deleteCar.addActionListener(e -> {

            deleteCar();
        });


        // =================================================
        // VIEW CUSTOMERS
        // =================================================

        viewCustomers.addActionListener(e -> {

            customerDAO.viewAllCustomers();

            JOptionPane.showMessageDialog(
                    this,
                    "Customer list displayed in the console."
            );
        });


        // =================================================
        // VIEW BOOKINGS
        // =================================================

        viewBookings.addActionListener(e -> {

            String input =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Customer ID:"
                    );

            if (input == null) {
                return;
            }

            try {

                int customerId =
                        Integer.parseInt(input);

                bookingDAO.viewBookingHistory(
                        customerId
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Booking history displayed in the console."
                );

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Customer ID."
                );
            }
        });


        // =================================================
        // VIEW PAYMENTS
        // =================================================

        viewPayments.addActionListener(e -> {

            String input =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Payment ID:"
                    );

            if (input == null) {
                return;
            }

            try {

                int paymentId =
                        Integer.parseInt(input);

                paymentDAO.viewPayment(
                        paymentId
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Payment details displayed in the console."
                );

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Payment ID."
                );
            }
        });


        // =================================================
        // LOGOUT
        // =================================================

        logout.addActionListener(e -> {

            dispose();

            new LoginFrame();
        });


        setVisible(true);
    }


    // =====================================================
    // SHOW CARS
    // =====================================================

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


        DefaultTableModel tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };


        for (Car car : cars) {

            tableModel.addRow(
                    new Object[]{
                            car.getCarId(),
                            car.getBrandName(),
                            car.getModelName(),
                            car.getCategoryName(),
                            car.getManufacturingYear(),
                            "₹" + car.getPricePerDay(),
                            car.getStatus()
                    }
            );
        }


        JTable table =
                new JTable(tableModel);

        table.setRowHeight(25);


        JScrollPane scrollPane =
                new JScrollPane(table);


        JDialog dialog =
                new JDialog(
                        this,
                        "All Cars",
                        true
                );

        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);

        dialog.add(scrollPane);

        dialog.setVisible(true);
    }


    // =====================================================
    // ADD CAR
    // =====================================================

    private void addCar() {

        try {

            String modelIdInput =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Model ID:"
                    );

            if (modelIdInput == null) {
                return;
            }


            String registration =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Registration Number:"
                    );

            if (registration == null) {
                return;
            }


            String yearInput =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Manufacturing Year:"
                    );

            if (yearInput == null) {
                return;
            }


            String priceInput =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Price Per Day:"
                    );

            if (priceInput == null) {
                return;
            }


            String status =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Status:\n"
                                    + "AVAILABLE\n"
                                    + "RENTED\n"
                                    + "MAINTENANCE"
                    );

            if (status == null) {
                return;
            }


            int modelId =
                    Integer.parseInt(modelIdInput);

            int year =
                    Integer.parseInt(yearInput);

            double price =
                    Double.parseDouble(priceInput);


            Car car =
                    new Car(
                            0,
                            modelId,
                            registration,
                            year,
                            price,
                            status,
                            "",
                            "",
                            ""
                    );


            carDAO.addCar(car);


            JOptionPane.showMessageDialog(
                    this,
                    "Car added successfully."
            );


        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid number entered."
            );
        }
    }


    // =====================================================
    // UPDATE CAR STATUS
    // =====================================================

    private void updateCarStatus() {

        String idInput =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Car ID:"
                );

        if (idInput == null) {
            return;
        }


        String status =
                JOptionPane.showInputDialog(
                        this,
                        "Enter new status:\n"
                                + "AVAILABLE\n"
                                + "RENTED\n"
                                + "MAINTENANCE"
                );

        if (status == null) {
            return;
        }


        try {

            int carId =
                    Integer.parseInt(idInput);


            carDAO.updateCarStatus(
                    carId,
                    status
            );


        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Car ID."
            );
        }
    }


    // =====================================================
    // DELETE CAR
    // =====================================================

    private void deleteCar() {

        String idInput =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Car ID:"
                );

        if (idInput == null) {
            return;
        }


        try {

            int carId =
                    Integer.parseInt(idInput);


            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete "
                                    + "Car ID "
                                    + carId
                                    + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );


            if (confirm ==
                    JOptionPane.YES_OPTION) {

                carDAO.deleteCar(carId);
            }


        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Car ID."
            );
        }
    }
}