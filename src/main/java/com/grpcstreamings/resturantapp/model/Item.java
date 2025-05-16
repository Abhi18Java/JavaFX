package com.grpcstreamings.resturantapp.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty description = new SimpleStringProperty();
    private final SimpleDoubleProperty price = new SimpleDoubleProperty();
    private final SimpleDoubleProperty vat = new SimpleDoubleProperty();


    public Item(int id, String name, String description, double price, double vat) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.price.set(price);
        this.vat.set(vat);
    }

    public Item(int itemId, String name, double unitPrice) {
        this.id.set(itemId);
        this.name.set(name);
        this.price.set(unitPrice);
    }

    // Property getters
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public SimpleDoubleProperty vatProperty() {
        return vat;
    }

    // Standard getters and setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public double getVat() {
        return vat.get();
    }

    public void setVat(double vat) {
        this.vat.set(vat);
    }

    @Override
    public String toString() {
        return name.get() +
                description.get() +
                price.get() +
                vat.get();
    }
}
