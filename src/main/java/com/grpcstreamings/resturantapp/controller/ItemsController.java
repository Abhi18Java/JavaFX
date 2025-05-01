package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.ItemDAO;
import com.grpcstreamings.resturantapp.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ItemsController {

    @FXML
    private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> descriptionColumn;
    @FXML private TableColumn<Item, Double> priceColumn;
    @FXML private TableColumn<Item, Double> vatColumn;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTable();
        loadItems();
        setupSearch();
    }

    private void configureTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellFactory(col -> new TableCell<Item, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? "" : String.format("$%.2f", price));
            }
        });
        vatColumn.setCellFactory(col -> new TableCell<Item, Double>() {
            @Override
            protected void updateItem(Double vat, boolean empty) {
                super.updateItem(vat, empty);
                setText(empty ? "" : String.format("%.2f%%", vat));
            }
        });
    }

    private void loadItems() {
        try {
            items.setAll(ItemDAO.getAllItems());
            itemsTable.setItems(items);
            statusLabel.setText("Loaded " + items.size() + " items");
        } catch (SQLException e) {
            showError("Database Error", "Failed to load items: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddItem() {
        showItemDialog(null);
    }

    @FXML
    private void handleEditItem() {
        Item selected = itemsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showItemDialog(selected);
        } else {
            showError("Selection Error", "Please select an item to edit");
        }
    }

    private void showItemDialog(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/grpcstreamings/resturantapp/fxml/item_dialog.fxml"));
            Parent root = loader.load();

            ItemDialogController controller = loader.getController();
            controller.setItem(item);

            Stage stage = new Stage();
            controller.setDialogStage(stage);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadItems(); // Refresh table after dialog closes
        } catch (IOException e) {
            showError("UI Error", "Could not load item dialog");
        }
    }

    public void prepareForm(Item item) {
        if (item == null) {
            // Initialize empty form for new item
        } else {
            // Populate form with existing item data
        }
    }

    @FXML
    private void handleDeleteItem() {
        Item selected = itemsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (showConfirmation("Delete Item",
                    "Are you sure you want to delete '" + selected.getName() + "'?")) {

                try {
                    ItemDAO.deleteItem(selected.getId());
                    loadItems();
                    statusLabel.setText("Item deleted successfully");
                } catch (SQLException e) {
                    showError("Delete Error", "Failed to delete item: " + e.getMessage());
                }
            }
        } else {
            showError("Selection Error", "Please select an item to delete");
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            FilteredList<Item> filtered = new FilteredList<>(items);
            filtered.setPredicate(item ->
                    item.getName().toLowerCase().contains(newVal.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(newVal.toLowerCase()));
            itemsTable.setItems(filtered);
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }

}
