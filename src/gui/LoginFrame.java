package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private UserDAO userDAO;

    public LoginFrame() {

        userDAO = new UserDAO();

        // Window settings
        setTitle("Car Rental System - Login");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel();
        panel.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        panel.setLayout(
                new GridLayout(4, 2, 10, 15)
        );

        // Components
        JLabel titleLabel =
                new JLabel("CAR RENTAL SYSTEM");

        JLabel usernameLabel =
                new JLabel("Username:");

        JLabel passwordLabel =
                new JLabel("Password:");

        usernameField =
                new JTextField();

        passwordField =
                new JPasswordField();

        loginButton =
                new JButton("LOGIN");


        // Add components to panel

        panel.add(titleLabel);
        panel.add(new JLabel());

        panel.add(usernameLabel);
        panel.add(usernameField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(new JLabel());
        panel.add(loginButton);


        // Add panel to window

        add(panel);


        // Login button action

        loginButton.addActionListener(e -> login());


        // Show window

        setVisible(true);
    }


    // ================= LOGIN =================

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        // Empty field check

        if (username.isEmpty() ||
                password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );

            return;
        }


        // Check database

        User user =
                userDAO.login(
                        username,
                        password
                );


        // Invalid login

        if (user == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            passwordField.setText("");

            return;
        }


        // Successful login

        JOptionPane.showMessageDialog(
                this,
                "Login successful!"
        );


        // Open dashboard based on role

        if (user.getRole().equalsIgnoreCase("ADMIN")) {

            new AdminDashboard(user);

        } else if (
                user.getRole().equalsIgnoreCase("CUSTOMER")
        ) {

            new CustomerDashboard(user);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid user role."
            );

            return;
        }


        // Close login window

        dispose();
    }


    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new LoginFrame()
        );
    }
}