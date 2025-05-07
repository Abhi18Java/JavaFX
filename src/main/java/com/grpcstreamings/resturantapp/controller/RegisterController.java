package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.UserDAO;
import com.grpcstreamings.resturantapp.model.User;
import com.grpcstreamings.resturantapp.util.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "All Fields are required!");
            return;
        }

        if (username.length() < 4 || username.length() > 20) {
            showAlert("Invalid Username",
                    "Username must be between 4-20 characters");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid Email",
                    "Invalid email format, Please enter correct email");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert("Password Requirements",
                    "Password must be at least 8 characters long and contain both letters and numbers");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Input Error", "Passwords do not match!");
            return;
        }

        try {
            if (UserDAO.findByUsername(username) != null) {
                showAlert("Input Error", "Username already exists!");
                return;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User newUser = new User(username, email, hashedPassword);
            UserDAO.createUser(newUser);

            showAlert("Success", "Registration successful!", Alert.AlertType.INFORMATION);
            // Switch to login screen
            switchToLogin(event);
        } catch (SQLException e) {
            showAlert("Database Error", "Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginLink(ActionEvent event) {
        try {
            SceneUtils.changeRoot(
                    event,
                    "/com/grpcstreamings/resturantapp/fxml/login.fxml"
            );
        } catch (IOException e) {
            showAlert("Navigation Error", "Error loading login screen");
        }
    }

    private void switchToLogin(ActionEvent event) {
        try {
            SceneUtils.changeRoot(
                    event,
                    "/com/grpcstreamings/resturantapp/fxml/login.fxml"
            );
        } catch (IOException e) {
            showAlert("Navigation Error", "Error loading login screen: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidPassword(String password) {
        // Minimum 8 characters, at least one letter and one number
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(pattern);
    }


}
