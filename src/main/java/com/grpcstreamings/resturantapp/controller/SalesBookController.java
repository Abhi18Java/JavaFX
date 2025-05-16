package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.SalesDAO;
import com.grpcstreamings.resturantapp.model.SaleHeader;
import com.grpcstreamings.resturantapp.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SalesBookController {

    @FXML
    private TableView<SaleHeader> salesTable;
    @FXML
    private TableColumn<SaleHeader, Integer> saleIdCol;
    @FXML
    private TableColumn<SaleHeader, LocalDateTime> dateCol;
    @FXML
    private TableColumn<SaleHeader, Double> totalCol;
    @FXML
    private TableColumn<SaleHeader, Double> discountCol;
    @FXML
    private TableColumn<SaleHeader, Double> tipCol;
    @FXML
    private TableColumn<SaleHeader, Double> taxCol;
    @FXML
    private TableColumn<SaleHeader, String> itemsCol;
    @FXML
    private TableColumn<SaleHeader, Void> actionCol;

    private ObservableList<SaleHeader> salesData = FXCollections.observableArrayList();
    private int userId = SessionManager.getCurrentUser().getId();

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        refreshSalesData();
    }

    private void setupTableColumns() {
        salesTable.setItems(salesData);
        // Sale ID Column
        saleIdCol.setCellValueFactory(cellData ->
                cellData.getValue().saleIdProperty().asObject());

        // Date Column with formatting
        dateCol.setCellValueFactory(cellData ->
                cellData.getValue().saleDateProperty());
        dateCol.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : formatter.format(item));
            }
        });

        // Numeric Columns
        totalCol.setCellValueFactory(cellData ->
                cellData.getValue().totalAmountProperty().asObject());
        discountCol.setCellValueFactory(cellData ->
                cellData.getValue().discountProperty().asObject());
        tipCol.setCellValueFactory(cellData ->
                cellData.getValue().tipProperty().asObject());
        taxCol.setCellValueFactory(cellData ->
                cellData.getValue().taxProperty().asObject());

        // Items Summary Column
        itemsCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(getItemsSummary(cellData.getValue())));

        // Action Column with View Button
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("View Details");

            {
                viewButton.setOnAction(event -> {
                    SaleHeader sale = getTableView().getItems().get(getIndex());
                    showItemsDialog(sale, event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        });
    }

    private String getItemsSummary(SaleHeader sale) {
        return sale.getItems().size() + " items";
    }

    public void refreshSalesData() {
        try {
            int currentUserId = SessionManager.getCurrentUser().getId();
            ObservableList<SaleHeader> newData = SalesDAO.getSalesHistory(currentUserId);
            salesData.setAll(newData);
        } catch (SQLException e) {
            showAlert("Refresh Error", "Could not update sales data: " + e.getMessage());
        }
    }

    private void showItemsDialog(SaleHeader sale, ActionEvent event) {
        try {

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grpcstreamings/resturantapp/fxml/sales_management.fxml"));
            Parent root = loader.load();

            NewSalesController controller = loader.getController();
            controller.initializeForEdit(sale);

            Stage stage = new Stage();
            stage.setTitle("Edit Sale");
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png"))
            ));
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Failed to open sales view" + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) {
        primaryStage.show();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}


/**
 * below code removed as now we want to redirect to Manage Sales Module when clicking on View Details
 */

//    private void showItemsDialog(SaleHeader sale) {
//        Dialog<Void> dialog = new Dialog<>();
//        dialog.setTitle("Sale Items - #" + sale.getSaleId());
//        dialog.setHeaderText("List of items purchased in this transaction:");
//
//        // Create TableView for items
//        TableView<SaleItem> itemsTable = new TableView<>();
//        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        // Item Name Column
//        TableColumn<SaleItem, String> nameCol = new TableColumn<>("Item");
//        nameCol.setCellValueFactory(cellData ->
//                cellData.getValue().getItem().nameProperty());
//
//        // Quantity Column
//        TableColumn<SaleItem, Integer> qtyCol = new TableColumn<>("Qty");
//        qtyCol.setCellValueFactory(cellData ->
//                cellData.getValue().quantityProperty().asObject());
//
//        // Unit Price Column
//        TableColumn<SaleItem, Double> priceCol = new TableColumn<>("Unit Price");
//        priceCol.setCellValueFactory(cellData ->
//                cellData.getValue().unitPriceProperty().asObject());
//        priceCol.setCellFactory(col -> new TableCell<>() {
//            @Override
//            protected void updateItem(Double price, boolean empty) {
//                super.updateItem(price, empty);
//                setText(empty ? null : String.format("$%.2f", price));
//            }
//        });
//

/// /        // Total Column
/// /        TableColumn<SaleItem, Double> totalCol = new TableColumn<>("Total");
/// /        totalCol.setCellValueFactory(cellData ->
/// /                cellData.getValue().lineTotalProperty().asObject());
/// /        totalCol.setCellFactory(col -> new TableCell<>() {
/// /            @Override
/// /            protected void updateItem(Double total, boolean empty) {
/// /                super.updateItem(total, empty);
/// /                setText(empty ? null : String.format("$%.2f", total));
/// /            }
/// /        });
//
//        itemsTable.getColumns().addAll(nameCol, qtyCol, priceCol);
//        itemsTable.setItems(FXCollections.observableArrayList(sale.getItems()));
//
//        // Dialog setup
//        dialog.getDialogPane().setContent(itemsTable);
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dialog.showAndWait();
//    }

