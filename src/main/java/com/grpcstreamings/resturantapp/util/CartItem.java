package com.grpcstreamings.resturantapp.util;

import com.grpcstreamings.resturantapp.model.Item;
import javafx.beans.property.*;

public class CartItem {
    private final ObjectProperty<Item> item;
    private final IntegerProperty quantity;
    private final DoubleProperty total;

    public CartItem(Item item, int quantity) {
        this.item = new SimpleObjectProperty<>(item);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.total = new SimpleDoubleProperty(item.getPrice() * quantity);

        // Bind total to quantity * item price
        total.bind(this.quantity.multiply(item.priceProperty()));
    }

    public Item getItem() { return item.get(); }
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int qty) { quantity.set(qty); }
    public IntegerProperty quantityProperty() { return quantity; }
    public double getTotal() { return total.get(); }
    public DoubleProperty totalProperty() { return total; }

}
