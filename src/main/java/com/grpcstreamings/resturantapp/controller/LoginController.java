package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.UserDAO;
import com.grpcstreamings.resturantapp.model.User;
import com.grpcstreamings.resturantapp.util.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

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

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/com/grpcstreamings/resturantapp/fxml/main_dashboard.fxml"
                        )
                );
                Parent root = loader.load();

                DashboardController controller = loader.getController();
                controller.setUsername(username);

                Stage mainStage = new Stage();
                mainStage.setTitle("Restaurant Management System");
                mainStage.setScene(new Scene(root));
                mainStage.setMaximized(true);
                mainStage.getIcons().add(new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png"))
                ));
                mainStage.show();

                // Close login window
                ((Node) event.getSource()).getScene().getWindow().hide();

            } catch (IOException e) {
                showAlert("Navigation Error", "Could not load main application: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Error during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
        try {
            SceneUtils.changeRoot(
                    event,
                    "/com/grpcstreamings/resturantapp/fxml/register.fxml"
            );
        } catch (IOException e) {
            showAlert("Navigation Error", "Error loading registration form");
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