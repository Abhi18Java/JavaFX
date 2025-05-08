package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.util.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private void handleItemsManagement(ActionEvent event) {
        try {

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/grpcstreamings/resturantapp/fxml/items_management.fxml"
            ));
            Parent root = loader.load();

            // Pass primary stage to the secondary controller
            ItemsController itemsController = loader.getController();
            itemsController.setPrimaryStage(primaryStage);

            // Configure the secondary stage
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setTitle("Items Management");
            secondaryStage.setMaximized(true);
            secondaryStage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png"))
            ));

            // Critical: Re-show primary stage when secondary is closed
            secondaryStage.setOnCloseRequest(e -> {
                primaryStage.show(); // Show primary stage when secondary is closed
            });

            secondaryStage.show();

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
    private void handleSalesManagement(ActionEvent event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/grpcstreamings/resturantapp/fxml/sales_management.fxml"
            ));
            Parent root = loader.load();

            // Pass primary stage to the secondary controller
            SalesController salesController = loader.getController();
            salesController.setPrimaryStage(primaryStage);

            // Configure the secondary stage
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setTitle("Sales Management");
            secondaryStage.setMaximized(true);
            secondaryStage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png"))
            ));

            secondaryStage.setOnCloseRequest(e -> {
                primaryStage.show();
            });

            secondaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
