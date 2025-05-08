package com.grpcstreamings.resturantapp.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

public class Sales {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleObjectProperty<Item> item = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty(1);
    private final SimpleDoubleProperty discount = new SimpleDoubleProperty();
    private final SimpleDoubleProperty tip = new SimpleDoubleProperty();
    private final SimpleDoubleProperty serviceTax = new SimpleDoubleProperty();
    private final SimpleObjectProperty<LocalDateTime> saleDate = new SimpleObjectProperty<>();

    public Sales() {
    }

    public Sales(int id, Item item, int quantity, double discount, double tip, double serviceTax, LocalDateTime saleDate) {
        this.id.set(id);
        this.item.set(item);
        this.quantity.set(quantity);
        this.discount.set(discount);
        this.tip.set(tip);
        this.serviceTax.set(serviceTax);
        this.saleDate.set(saleDate);
    }


    public double calculateFinalPrice() {
        double basePrice = item.get().getPrice() * quantity.get();
        double vatAmount = basePrice * item.get().getVat() / 100;
        double priceWithVat = basePrice + vatAmount;
        double discountAmount = priceWithVat * discount.get() / 100;
        double serviceTaxAmount = priceWithVat * serviceTax.get() / 100;
        return priceWithVat - discountAmount + serviceTaxAmount + tip.get();
    }

    //Property Getter Methods

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleObjectProperty<Item> itemProperty() {
        return item;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleDoubleProperty discountProperty() {
        return discount;
    }

    public SimpleDoubleProperty tipProperty() {
        return tip;
    }

    public SimpleDoubleProperty serviceTaxProperty() {
        return serviceTax;
    }

    public SimpleObjectProperty<LocalDateTime> saleDateProperty() {
        return saleDate;
    }

    //Simple Getters

    public int getId() {
        return id.get();
    }

    public Item getItem() {
        return item.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public double getDiscount() {
        return discount.get();
    }

    public double getTip() {
        return tip.get();
    }

    public double getServiceTax() {
        return serviceTax.get();
    }

    public LocalDateTime getSaleDate() {
        return saleDate.get();
    }

    //Simple Getters

    public void setId(int id) {
        this.id.set(id);
    }

    public void setItem(Item item) {
        this.item.set(item);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }

    public void setTip(double tip) {
        this.tip.set(tip);
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax.set(serviceTax);
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate.set(saleDate);
    }

}
