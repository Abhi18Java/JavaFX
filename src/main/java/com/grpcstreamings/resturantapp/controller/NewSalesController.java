package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.ItemDAO;
import com.grpcstreamings.resturantapp.dao.SalesDAO;
import com.grpcstreamings.resturantapp.model.Item;
import com.grpcstreamings.resturantapp.model.SaleHeader;
import com.grpcstreamings.resturantapp.model.SaleItem;
import com.grpcstreamings.resturantapp.util.CartItem;
import com.grpcstreamings.resturantapp.util.SessionManager;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

public class NewSalesController {

    @FXML
    private VBox menuItemsContainer;
    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, String> itemCol;
    @FXML
    private TableColumn<CartItem, Number> priceCol;
    @FXML
    private TableColumn<CartItem, Integer> qtyCol;
    @FXML
    private Label balanceLabel;
    @FXML
    private TextField discountField;
    @FXML
    private Label vatLabel;
    @FXML
    private TextField tipField;
    @FXML
    private Label totalLabel;

    @FXML
    private Button completeSaleBtn;

    private final BooleanProperty isEditMode = new SimpleBooleanProperty(false);
    private SaleHeader existingSale;

    private Stage primaryStage;

    int userId = SessionManager.getCurrentUser().getId();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private final ObservableList<CartItem> cartItems =
            FXCollections.observableArrayList(cartItem ->
                    new Observable[]{cartItem.totalProperty()}
            );

    @FXML
    public void initialize() {
        configureCartTable();
        loadItems();
        setupCalculations();

        completeSaleBtn.textProperty().bind(
                Bindings.when(isEditMode)
                        .then("Update Sale")
                        .otherwise("Complete Sale")
        );
    }

    public void initializeForEdit(SaleHeader sale) {
        this.isEditMode.set(true);
        this.existingSale = sale;

        // Load existing items into cart
        sale.getItems().forEach(saleItem -> {
            CartItem cartItem = new CartItem(saleItem.getItem(), saleItem.getQuantity());
            cartItems.add(cartItem);
        });

        // Set other fields
        discountField.setText(String.valueOf(sale.getDiscount()));
        tipField.setText(String.valueOf(sale.getTip()));
    }

    private void configureCartTable() {
        itemCol.setCellValueFactory(cellData ->
                cellData.getValue().getItem().nameProperty());
        priceCol.setCellValueFactory(cellData ->
                cellData.getValue().getItem().priceProperty());
        qtyCol.setCellValueFactory(cellData ->
                cellData.getValue().quantityProperty().asObject());

        // Make quantity column editable
        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        qtyCol.setOnEditCommit(event -> {
            CartItem item = event.getRowValue();
            item.setQuantity(event.getNewValue());
        });

        cartTable.setItems(cartItems);
        cartTable.setEditable(true);
    }

    private void loadItems() {
        try {
            ObservableList<Item> items = ItemDAO.getAllItems(userId);
            menuItemsContainer.getChildren().clear();

            for (Item item : items) {
                HBox row = new HBox();
                row.setSpacing(10);
                row.setPadding(new Insets(8, 15, 8, 15));
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");
                row.setMaxWidth(Double.MAX_VALUE);

                Label nameLabel = new Label(item.getName());
                nameLabel.setStyle("-fx-font-size: 14;");

                Region spacer = new Region(); // This pushes buttons to the right
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button addBtn = new Button("+");
                Button removeBtn = new Button("-");

                addBtn.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
                removeBtn.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

                addBtn.getStyleClass().addAll("button", "add-btn");
                removeBtn.getStyleClass().addAll("button", "remove-btn");

                addBtn.setOnAction(e -> addToCart(item));
                removeBtn.setOnAction(e -> removeFromCart(item));

                row.getChildren().addAll(nameLabel, spacer, removeBtn, addBtn);
                menuItemsContainer.getChildren().add(row);
            }
        } catch (SQLException e) {
            showError("Error Loading Items", e.getMessage());
        }
    }

    private void addToCart(Item item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getItem().equals(item)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(item, 1));
    }

    private void removeFromCart(Item item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getItem().equals(item)) {
                int currentQty = cartItem.getQuantity();
                if (currentQty > 1) {
                    cartItem.setQuantity(currentQty - 1);
                } else {
                    cartItems.remove(cartItem);
                }
                break;
            }
        }
    }

    private void setupCalculations() {
        // Balance = sum of all items' totals
        DoubleBinding balance = Bindings.createDoubleBinding(() ->
                cartItems.stream().mapToDouble(CartItem::getTotal).sum(), cartItems);

        DoubleBinding discount = Bindings.createDoubleBinding(() -> {
            try {
                return Double.parseDouble(discountField.getText());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }, discountField.textProperty());

        // VAT is 13% of balance
        DoubleBinding vat = balance.multiply(0.13);

        // Tip calculation
        DoubleBinding tip = Bindings.createDoubleBinding(() -> {
            try {
                return Double.parseDouble(tipField.getText());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }, tipField.textProperty());

        // Total calculation
        DoubleBinding total = balance.subtract(balance.multiply(discount.divide(100)))
                .add(vat)
                .add(tip);

        balanceLabel.textProperty().bind(Bindings.format("$%.2f", balance));
        vatLabel.textProperty().bind(Bindings.format("$%.2f", vat));
        totalLabel.textProperty().bind(Bindings.format("$%.2f", total));

    }

    @FXML
    public void handleCompleteSale(ActionEvent event) {
        if (cartItems.isEmpty()) {
            showError("Empty Cart", "Please add Items to the cart");
            return;
        }
        try {
            double subtotal = cartItems.stream()
                    .mapToDouble(CartItem::getTotal)
                    .sum();
            double discount = tryParseDouble(discountField.getText(), 0.0);
            double tip = tryParseDouble(tipField.getText(), 0.0);
            double vat = subtotal * 0.13;
            double total = subtotal - discount + vat + tip;

            ObservableList<SaleItem> saleItems = FXCollections.observableArrayList();
            for (CartItem cartItem : cartItems) {
                Item item = cartItem.getItem();
                saleItems.add(new SaleItem(
                        item,
                        cartItem.getQuantity(),
                        item.getPrice()
                ));
            }

            SaleHeader saleHeader = new SaleHeader(
                    isEditMode.get() ? existingSale.getSaleId() : 0,
                    isEditMode.get() ? existingSale.getSaleDate() : LocalDateTime.now(),
                    subtotal,
                    discount,
                    tip,
                    vat,
                    total,
                    userId,
                    saleItems
            );

            if (isEditMode.get()) {
                SalesDAO.updateSale(saleHeader);
            } else {
                SalesDAO.saveSale(saleHeader);
            }

            // Reset UI
            cartItems.clear();
            discountField.clear();
            tipField.clear();

            if (isEditMode.get()) {
                try {
                    // Close current window
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();

                    // Reload Sales Book
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grpcstreamings/resturantapp/fxml/sales_book.fxml"));
                    Parent root = loader.load();

                    // Get controller and refresh
                    SalesBookController bookController = loader.getController();
                    bookController.refreshSalesData();

                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.setMaximized(true);
                    newStage.getIcons().add(new Image(
                            Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png"))
                    ));
                    newStage.show();

                } catch (IOException e) {
                    showError("Navigation Error", "Failed to load sales book: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            showError("Database Error", "Failed to save sale: " + e.getMessage());
            e.printStackTrace(); // Log for debugging
        } catch (NumberFormatException e) {
            showError("Input Error", "Invalid number format in discount/tip fields");
        } catch (Exception e) {
            showError("Error", "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSalesBook() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/grpcstreamings/resturantapp/fxml/sales_book.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Navigation Error", "Failed to open sales book");
        }
    }

    private SaleCalculation computeSaleTotals() {
        double subtotal = cartItems.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();

        double discount = tryParseDouble(discountField.getText(), 0.0);
        double tip = tryParseDouble(tipField.getText(), 0.0);
        double vat = subtotal * 0.13;
        double total = subtotal - discount + vat + tip;

        return new SaleCalculation(subtotal, discount, tip, vat, total);
    }

    private double tryParseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.isEmpty() ? "0" : value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        primaryStage.show();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class SaleCalculation {
        final double subtotal, discount, tip, vat, total;

        public SaleCalculation(double subtotal, double discount, double tip, double vat, double total) {
            this.subtotal = subtotal;
            this.discount = discount;
            this.tip = tip;
            this.vat = vat;
            this.total = total;
        }
    }

    private static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
