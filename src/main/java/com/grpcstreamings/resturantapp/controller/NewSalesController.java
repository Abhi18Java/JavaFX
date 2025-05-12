package com.grpcstreamings.resturantapp.controller;

import com.grpcstreamings.resturantapp.dao.ItemDAO;
import com.grpcstreamings.resturantapp.model.Item;
import com.grpcstreamings.resturantapp.util.CartItem;
import com.grpcstreamings.resturantapp.util.SessionManager;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;

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
                row.setPadding(new Insets(5));
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");
                row.setMaxWidth(Double.MAX_VALUE);

                Label nameLabel = new Label(item.getName());
                nameLabel.setStyle("-fx-font-size: 14;");

                Region spacer = new Region(); // This pushes buttons to the right
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button addBtn = new Button("Add");
                Button removeBtn = new Button("Remove");

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
                cartItems.stream().mapToDouble(CartItem::getTotal).sum(),cartItems);

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
        DoubleBinding tip = Bindings.createDoubleBinding(()-> {
            try {
                return Double.parseDouble(tipField.getText());
            }catch (NumberFormatException e) {
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

}
