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

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields");
            return;
        }

        if (username.length() < 4 || username.length() > 20) {
            showAlert("Invalid Username",
                    "Username must be between 4-20 characters");
            return;
        }

        try {
            User user = UserDAO.findByUsername(username);
            if (user == null || !BCrypt.checkpw(password, user.getPasswordHash())) {
                showAlert("Login Failed", "Invalid username or password");
                return;
            }

            // Successful login - proceed to main application
            showAlert("Login Successful", "Welcome back " + username, Alert.AlertType.INFORMATION);
            // TODO: Switch to main application screen
            // SceneUtils.changeScene(event, "/com/grpcstreamings/resturantapp/main.fxml", "Main");

        } catch (SQLException e) {
            showAlert("Database Error", "Error during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
        try {
            SceneUtils.changeScene(event,
                    "/com/grpcstreamings/resturantapp/fxml/register.fxml",
                    "Register");
        } catch (IOException e) {
            showAlert("Navigation Error", "Error loading registration form: " + e.getMessage());
        }
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

}