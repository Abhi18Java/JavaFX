package com.grpcstreamings.resturantapp.dao;

import com.grpcstreamings.resturantapp.model.Item;
import com.grpcstreamings.resturantapp.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ItemDAO {

    public static void createItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (name, description, price, vat) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setDouble(4, item.getVat());
            stmt.executeUpdate();
        }
    }

    public static ObservableList<Item> getAllItems() throws SQLException {
        ObservableList<Item> items = FXCollections.observableArrayList();
        String sql = "SELECT * FROM items";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getDouble("vat")
                ));
            }
        }
        return items;
    }

    public static void updateItem(Item item) throws SQLException {
        String slq = "UPDATE items SET name = ?, description = ?, price = ?, vat = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(slq);

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setDouble(4, item.getVat());
            stmt.setInt(5, item.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteItem(int id) throws SQLException {
        String sql = "Delete from items WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.executeUpdate();

        }
    }

}
