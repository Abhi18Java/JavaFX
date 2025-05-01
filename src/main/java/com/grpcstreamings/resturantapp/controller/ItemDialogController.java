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
                if (currentItem == null) {
                    Item newItem = new Item(0,
                            nameField.getText(),
                            descriptionField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Double.parseDouble(vatField.getText())
                    );
                    ItemDAO.createItem(newItem);
                } else { // Existing item
                    currentItem.setName(nameField.getText());
                    currentItem.setDescription(descriptionField.getText());
                    currentItem.setPrice(Double.parseDouble(priceField.getText()));
                    currentItem.setVat(Double.parseDouble(vatField.getText()));
                    ItemDAO.updateItem(currentItem);
                }
            }catch (SQLException | NumberFormatException e) {
                showError("Save Error", "Failed to save item: " + e.getMessage());

            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
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
        }catch (NumberFormatException e) {
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
