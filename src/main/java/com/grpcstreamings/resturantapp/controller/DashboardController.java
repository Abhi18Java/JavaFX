package com.grpcstreamings.resturantapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            stage.centerOnScreen();
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png"))
            ));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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

}
