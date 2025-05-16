package com.grpcstreamings.resturantapp.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SaleItem {

    private final SimpleIntegerProperty saleItemId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty saleId = new SimpleIntegerProperty();
    private final SimpleObjectProperty<Item> item = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    private final SimpleDoubleProperty unitPrice = new SimpleDoubleProperty();
    private final SimpleDoubleProperty lineTotal = new SimpleDoubleProperty();

    public SaleItem() {
    }

    public SaleItem(int saleItemId, int saleId, Item item, int quantity, double unitPrice, double lineTotal) {
        this.saleItemId.set(saleItemId);
        this.saleId.set(saleId);
        this.item.set(item);
        this.quantity.set(quantity);
        this.unitPrice.set(unitPrice);
        this.lineTotal.set(lineTotal);
    }

    public SaleItem(Item item, int quantity, double unitPrice) {
        this.item.set(item);
        this.quantity.set(quantity);
        this.unitPrice.set(unitPrice);
    }

    public SimpleIntegerProperty saleItemIdProperty() {
        return saleItemId;
    }

    public SimpleIntegerProperty saleIdProperty() {
        return saleId;
    }

    public SimpleObjectProperty<Item> itemProperty() {
        return item;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleDoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public SimpleDoubleProperty lineTotalProperty() {
        return lineTotal;
    }

    public int getSaleItemId() {
        return saleItemId.get();
    }

    public int getSaleId() {
        return saleId.get();
    }

    public Item getItem() {
        return item.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public double getLineTotal() {
        return lineTotal.get();
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId.set(saleItemId);
    }

    public void setSaleId(int saleId) {
        this.saleId.set(saleId);
    }

    public void setItem(Item item) {
        this.item.set(item);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal.set(lineTotal);
    }

}
