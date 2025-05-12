package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.ItemDAO;
import com.grpcstreamings.resturantapp.dao.SalesDAO;
import com.grpcstreamings.resturantapp.model.Item;
import com.grpcstreamings.resturantapp.model.Sales;
import com.grpcstreamings.resturantapp.util.SessionManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalesController {
//    @FXML
//    private TableView<Sales> salesTable;
//    @FXML
//    private ComboBox<Item> itemsCombo;
//    @FXML
//    private TextField quantityField;
//    @FXML
//    private TextField discountField;
//    @FXML
//    private TextField serviceTaxField;
//    @FXML
//    private TextField tipField;
//    @FXML
//    private Label totalLabel;
//    @FXML
//    private TableColumn<Sales, String> itemColumn;
//    @FXML
//    private TableColumn<Sales, Number> quantityColumn;
//    @FXML
//    private TableColumn<Sales, Number> totalColumn;
//    @FXML
//    private TableColumn<Sales, LocalDateTime> dateColumn;
//
//    private ObservableList<Sales> sales = FXCollections.observableArrayList();
//
//    private Stage primaryStage;
//
//    public void setPrimaryStage(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//    }
//
//    int userId = SessionManager.getCurrentUser().getId();
//
//    @FXML
//    public void initialize() {
//        configureItemsCombo();
//        loadItems();
//        configureTable();
//        loadSales();
//    }
//
//    @FXML
//    private void handleCalculate() {
//        try {
//            if (quantityField.getText().isEmpty() || serviceTaxField.getText().isEmpty()) {
//                showError("Input Error", "Quantity and Service Tax are required");
//                return;
//            }
//            Sales sale = new Sales();
//            sale.setItem(itemsCombo.getValue());
//            //Required Fields
//            int quantity = Integer.parseInt(quantityField.getText());
//            double serviceTax = Double.parseDouble(serviceTaxField.getText());
//            //Optional Fields
//            double discount = discountField.getText().isEmpty() ? 0.0 :
//                    Double.parseDouble(discountField.getText());
//            double tip = tipField.getText().isEmpty() ? 0.0 :
//                    Double.parseDouble(tipField.getText());
//
//            sale.setQuantity(quantity);
//            sale.setDiscount(discount);
//            sale.setServiceTax(serviceTax);
//            sale.setTip(tip);
//
//            totalLabel.setText(String.format("Total: $%.2f", sale.calculateFinalPrice()));
//        } catch (NumberFormatException e) {
//            showError("Input Error", "Please enter valid numbers");
//        }
//    }
//
//    @FXML
//    private void handleNewSale() {
//        if (validateInput()) {
//            try {
//                Sales sale = createSaleForm();
//                SalesDAO.createSale(sale, userId);
//                sales.add(sale);
//                clearForm(); // This will reset the ComboBox
//            } catch (SQLException e) {
//                showError("Database Error", "Failed to save sale");
//            }
//        }
//    }
//
//    @FXML
//    private void handleBack(ActionEvent event) {
//        primaryStage.show();
//        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        currentStage.close();
//    }
//
//    private void configureItemsCombo() {
//        // Custom cell factory for dropdown items (shows detailed view)
//        itemsCombo.setCellFactory(lv -> new ListCell<Item>() {
//            private final Label nameLabel = new Label();
//            private final Label descLabel = new Label();
//            private final Label priceLabel = new Label();
//            private final VBox container = new VBox(nameLabel, descLabel, priceLabel);
//
//            {
//                // Style adjustments
//                nameLabel.setStyle("-fx-font-weight: bold;");
//                descLabel.setStyle("-fx-text-fill: #666;");
//                priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
//                container.setSpacing(2);
//            }
//
//            @Override
//            protected void updateItem(Item item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setGraphic(null);
//                } else {
//                    nameLabel.setText(item.getName());
//                    descLabel.setText(item.getDescription());
//                    priceLabel.setText(String.format("$%.2f", item.getPrice()));
//                    setGraphic(container);
//                }
//            }
//        });
//
//        // Button cell (selected value) shows only the name
//        itemsCombo.setButtonCell(new ListCell<Item>() {
//            @Override
//            protected void updateItem(Item item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    if (itemsCombo.getPromptText() != null && !itemsCombo.getPromptText().isEmpty()) {
//                        setText(itemsCombo.getPromptText());
//                        setStyle("-fx-text-fill: derive(-fx-control-inner-background, -30%);");
//                    } else {
//                        setText("");
//                    }
//                } else {
//                    setText(item.getName());
//                    setStyle("");
//                }
//            }
//        });
//    }
//
//    private void configureTable() {
//        // Item Column
//        itemColumn.setCellValueFactory(cellData ->
//                cellData.getValue().getItem().nameProperty());
//
//        // Quantity Column
//        quantityColumn.setCellValueFactory(cellData ->
//                cellData.getValue().quantityProperty());
//
//        // Total Column
//        totalColumn.setCellValueFactory(cellData ->
//                new SimpleDoubleProperty(cellData.getValue().calculateFinalPrice()));
//        totalColumn.setCellFactory(col -> new TableCell<Sales, Number>() {
//            @Override
//            protected void updateItem(Number price, boolean empty) {
//                super.updateItem(price, empty);
//                setText(empty ? "" : String.format("$%.2f", price.doubleValue()));
//            }
//        });
//
//        // Date Column
//        dateColumn.setCellValueFactory(cellData ->
//                cellData.getValue().saleDateProperty());
//        dateColumn.setCellFactory(col -> new TableCell<Sales, LocalDateTime>() {
//            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//
//            @Override
//            protected void updateItem(LocalDateTime date, boolean empty) {
//                super.updateItem(date, empty);
//                setText(empty ? "" : date.format(formatter));
//            }
//        });
//    }
//
//
//    private void loadSales() {
//        try {
//            sales.setAll(SalesDAO.getAllSales(userId));
//            salesTable.setItems(sales);
//        } catch (SQLException e) {
//            showError("Database Error", "Failed to load sales: " + e.getMessage());
//        }
//    }
//
//
//    private boolean validateInput() {
//        StringBuilder errors = new StringBuilder();
//
//        // Required fields
//        if (itemsCombo.getValue() == null) {
//            errors.append("- Please select an item\n");
//        }
//
//        // Quantity validation
//        try {
//            int quantity = Integer.parseInt(quantityField.getText());
//            if (quantity < 1) {
//                errors.append("- Quantity must be at least 1\n");
//            }
//        } catch (NumberFormatException e) {
//            errors.append("- Invalid quantity format\n");
//        }
//
//        // Discount validation (optional)
//        if (!discountField.getText().isEmpty()) {
//            try {
//                double discount = Double.parseDouble(discountField.getText());
//                if (discount < 0 || discount > 100) {
//                    errors.append("- Discount must be between 0-100%\n");
//                }
//            } catch (NumberFormatException e) {
//                errors.append("- Invalid discount format\n");
//            }
//        }
//
//        // Service Tax validation
//        try {
//            double tax = Double.parseDouble(serviceTaxField.getText());
//            if (tax < 0 || tax > 100) {
//                errors.append("- Service tax must be between 0-100%\n");
//            }
//        } catch (NumberFormatException e) {
//            errors.append("- Invalid service tax format\n");
//        }
//
//        // Tip validation (optional)
//        if (!tipField.getText().isEmpty()) {
//            try {
//                double tip = Double.parseDouble(tipField.getText());
//                if (tip < 0) {
//                    errors.append("- Tip cannot be negative\n");
//                }
//            } catch (NumberFormatException e) {
//                errors.append("- Invalid tip format\n");
//            }
//        }
//
//        if (errors.length() > 0) {
//            showError("Validation Error", "Please fix the following errors:\n\n" + errors.toString());
//            return false;
//        }
//        return true;
//    }
//
//    private void loadItems() {
//        try {
//            itemsCombo.setItems(ItemDAO.getAllItems(userId));
//        } catch (SQLException e) {
//            showError("Database Error", "Failed to load items");
//        }
//    }
//
//    private void showError(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private Sales createSaleForm() {
//        Item item = itemsCombo.getValue();
//        int quantity = Integer.parseInt(quantityField.getText());
//        double discount = discountField.getText().isEmpty() ? 0.0 : Double.parseDouble(discountField.getText());
//        double tip = tipField.getText().isEmpty() ? 0.0 : Double.parseDouble(tipField.getText());
//        double serviceTax = Double.parseDouble(serviceTaxField.getText());
//
//        return new Sales(0, item, quantity, discount, tip, serviceTax, LocalDateTime.now(), userId);
//    }
//
//    private void clearForm() {
//        itemsCombo.setValue(null);
//        itemsCombo.setPromptText("Select Item"); // Explicitly reset
//        itemsCombo.setVisible(false);
//        itemsCombo.setVisible(true); // Explicitly reapply prompt
//        itemsCombo.getStyleClass().add("force-prompt"); // Workaround CSS
//        quantityField.setText("1");
//        discountField.clear();
//        serviceTaxField.clear();
//        tipField.clear();
//        totalLabel.setText("");
//
//        // JavaFX hack: Toggle visibility to force UI refresh
//        itemsCombo.setVisible(false);
//        itemsCombo.setVisible(true);
//
//        // Force focus shift
//        quantityField.requestFocus();
//    }

}
