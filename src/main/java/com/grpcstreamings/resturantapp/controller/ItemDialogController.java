package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.ItemDAO;
import com.grpcstreamings.resturantapp.model.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ItemDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField vatField;

    private Item currentItem;
    private Stage dialogStage;
    private boolean saved = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setItem(Item item) {
        this.currentItem = item;
        if (item != null) {
            nameField.setText(item.getName());
            descriptionField.setText(item.getDescription());
            priceField.setText(String.valueOf(item.getPrice()));
            vatField.setText(String.valueOf(item.getVat()));
        }
    }

    @FXML
    private void handleSaveItem() {
        if (validateInput()) {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                double vat = Double.parseDouble(vatField.getText());

                if (currentItem == null) {
                    // Check for duplicate for new item
                    if (ItemDAO.isExist(name, description, price)) {
                        showAlert("Duplicate Item", "An item with the same name, description, and price already exists.", Alert.AlertType.ERROR);
                        return;
                    }
                    Item newItem = new Item(0, name, description, price, vat);
                    ItemDAO.createItem(newItem);
                    showAlert("Success", "Item added successfully!", Alert.AlertType.INFORMATION);
                } else {
                    if (ItemDAO.isExist(name, description, price)) {
                        showAlert("Duplicate Item", "Another item with the same name, description, and price already exists.", Alert.AlertType.ERROR);
                        return;
                    }
                    currentItem.setName(name);
                    currentItem.setDescription(description);
                    currentItem.setPrice(price);
                    currentItem.setVat(vat);
                    ItemDAO.updateItem(currentItem);
                    showAlert("Success", "Item updated successfully!", Alert.AlertType.INFORMATION);
                }
                saved = true;
                dialogStage.close();
            } catch (SQLException | NumberFormatException e) {
                showError("Save Error", "Failed to save item: " + e.getMessage());
            }
            finally {
                dialogStage.close();
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public boolean isSaved() {
        return saved;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(dialogStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateInput() {

        StringBuilder errors = new StringBuilder();

        if (nameField.getText().isEmpty()) {
            errors.append("Name is required\n");
        }

        try {
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            errors.append("Invalid price format");
        }

        try {
            Double vat = Double.parseDouble(vatField.getText());
            if (vat < 0 || vat > 100) {
                errors.append("Vat must be between 0-100\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Invalid VAT format\n");
        }

        if (errors.length() > 0) {
            showError("Validation Error", errors.toString());
            return false;
        }
        return true;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
