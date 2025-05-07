package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.util.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashboardController {

    @FXML
    private Label usernameLabel;

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    @FXML
    private void handleItemsManagement() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource(
                            "/com/grpcstreamings/resturantapp/fxml/items_management.fxml"
                    )
            );
            Stage stage = new Stage();
            stage.setTitle("Items Management");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png"))
            ));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        boolean confirmed = showConfirmation("Logout", "Are you sure you want to Logout ?");
        if (confirmed) {
            try {
                SceneUtils.changeRoot(event,
                        "/com/grpcstreamings/resturantapp/fxml/login.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSalesManagement() {
        // Implement sales management navigation
    }

    @FXML
    private void handleReports() {
        // Implement reports navigation
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }

}
