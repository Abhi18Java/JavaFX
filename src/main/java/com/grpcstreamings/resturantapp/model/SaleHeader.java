package com.grpcstreamings.resturantapp.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class SaleHeader {
    private final SimpleIntegerProperty saleId = new SimpleIntegerProperty();
    private final SimpleObjectProperty<LocalDateTime> saleDate = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty subtotal = new SimpleDoubleProperty();
    private final SimpleDoubleProperty discount = new SimpleDoubleProperty();
    private final SimpleDoubleProperty tip = new SimpleDoubleProperty();
    private final SimpleDoubleProperty tax = new SimpleDoubleProperty();
    private final SimpleDoubleProperty totalAmount = new SimpleDoubleProperty();
    private final SimpleIntegerProperty userId = new SimpleIntegerProperty();
    private final ObservableList<SaleItem> items = FXCollections.observableArrayList();

    public SaleHeader() {
    }

    public SaleHeader(int saleId, LocalDateTime saleDate, double subtotal,
                      double discount, double tip, double tax, double totalAmount,
                      int userId, ObservableList<SaleItem> items) {
        this.saleId.set(saleId);
        this.saleDate.set(saleDate);
        this.subtotal.set(subtotal);
        this.discount.set(discount);
        this.tip.set(tip);
        this.tax.set(tax);
        this.totalAmount.set(totalAmount);
        this.userId.set(userId);
        this.items.setAll(items);
    }

    public SimpleIntegerProperty saleIdProperty() {
        return saleId;
    }

    public SimpleObjectProperty<LocalDateTime> saleDateProperty() {
        return saleDate;
    }

    public SimpleDoubleProperty subTotalProperty() {
        return subtotal;
    }

    public SimpleDoubleProperty discountProperty() {
        return discount;
    }

    public SimpleDoubleProperty tipProperty() {
        return tip;
    }

    public SimpleDoubleProperty taxProperty() {
        return tax;
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public ObservableList<SaleItem> getItems() {
        return items;
    }

    public int getSaleId() {
        return saleId.get();
    }

    public LocalDateTime getSaleDate() {
        return saleDate.get();
    }

    public double getSubtotal() {
        return subtotal.get();
    }

    public SimpleDoubleProperty subtotalProperty() {
        return subtotal;
    }

    public double getDiscount() {
        return discount.get();
    }

    public double getTip() {
        return tip.get();
    }

    public double getTax() {
        return tax.get();
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public int getUserId() {
        return userId.get();
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate.set(saleDate);
    }

    public void setSubtotal(double subtotal) {
        this.subtotal.set(subtotal);
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }

    public void setTip(double tip) {
        this.tip.set(tip);
    }

    public void setTax(double tax) {
        this.tax.set(tax);
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public void setSaleId(int saleId) {
        this.saleId.set(saleId);
    }

    public void setItems(ObservableList<SaleItem> items) {
        this.items.setAll(items);
    }

}
