package view;

import controller.StockController;
import controller.UserController;
import dao.PersonalStockDAO;
import dao.StockDAO;
import dao.StockTransactionDAO;
import dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class UserRegistrationLoginView {
    private UserController userController;
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextField usernameField, initialBalanceField, registrationUsernameField;

    private JPasswordField passwordField, registrationPasswordField;

    public UserRegistrationLoginView(UserController userController) {
        this.userController = userController;

        frame = new JFrame("User Registration and Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frame.add(cardPanel);

        JPanel registrationPanel = createRegistrationPanel();
        JPanel loginPanel = createLoginPanel();

        cardPanel.add(registrationPanel, "REGISTRATION");
        cardPanel.add(loginPanel, "LOGIN");

        cardLayout.show(cardPanel, "LOGIN");

        frame.setVisible(true);
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 20, 80, 25);
        panel.add(usernameLabel);

        registrationUsernameField = new JTextField(20);  // Thay đổi tên trường này
        registrationUsernameField.setBounds(100, 20, 165, 25);
        panel.add(registrationUsernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        registrationPasswordField = new JPasswordField(20);
        registrationPasswordField.setBounds(100, 50, 165, 25);
        panel.add(registrationPasswordField);

        JLabel initialBalanceLabel = new JLabel("Initial Balance:");
        initialBalanceLabel.setBounds(10, 80, 120, 25);
        panel.add(initialBalanceLabel);

        initialBalanceField = new JTextField(20);
        initialBalanceField.setBounds(100, 80, 165, 25);
        panel.add(initialBalanceField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(10, 110, 120, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        panel.add(registerButton);

        // Add a label and button to switch to the login panel
        JLabel switchToLoginLabel = new JLabel("Already have an account? Log in here:");
        switchToLoginLabel.setBounds(10, 140, 250, 25);
        panel.add(switchToLoginLabel);

        JButton switchToLoginButton = new JButton("Login");
        switchToLoginButton.setBounds(260, 140, 100, 25);
        switchToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "LOGIN");
            }
        });
        panel.add(switchToLoginButton);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 20, 80, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 120, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        panel.add(loginButton);

        // Add a label and button to switch to the registration panel
        JLabel switchToRegistrationLabel = new JLabel("Don't have an account? Register here:");
        switchToRegistrationLabel.setBounds(10, 110, 250, 25);
        panel.add(switchToRegistrationLabel);

        JButton switchToRegistrationButton = new JButton("Register");
        switchToRegistrationButton.setBounds(260, 110, 100, 25);
        switchToRegistrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "REGISTRATION");
            }
        });
        panel.add(switchToRegistrationButton);

        return panel;
    }

    private void handleRegistration() {
        try {
            String username = registrationUsernameField.getText();
            String password = new String(registrationPasswordField.getPassword());
            BigDecimal initialBalance = new BigDecimal(initialBalanceField.getText());

            // Kiểm tra xem username và password có được nhập hay không
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password are required fields.");
                return; // Không thực hiện đăng ký nếu thông tin không đầy đủ
            }

            if (userController.registerUser(username, password, initialBalance)) {
                JOptionPane.showMessageDialog(frame, "Registration successful for user '" + username + "'. You can now log in.");
                cardLayout.show(cardPanel, "LOGIN");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
        }
    }

    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (userController.validateLogin(username, password)) {
                int userId = userController.getUserIdByUserName(username);
                JOptionPane.showMessageDialog(frame, "Login successful for user '" + username + "'.");

                // Dispose the current frame (UserRegistrationLoginView)
                frame.dispose();

                // Open the StockAppView
                SwingUtilities.invokeLater(() -> new StockAppView(
                        new StockController(new StockDAO(), new StockTransactionDAO(), new UserDAO(), new PersonalStockDAO()), userId));
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid login credentials. Please try again.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserRegistrationLoginView(new UserController(new UserDAO()));
            }
        });
    }
}