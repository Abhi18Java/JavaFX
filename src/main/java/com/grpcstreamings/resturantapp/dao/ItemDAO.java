package com.grpcstreamings.resturantapp.dao;

import com.grpcstreamings.resturantapp.model.Item;
import com.grpcstreamings.resturantapp.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ItemDAO {

    public static void createItem(Item item, int userId) throws SQLException {
        String sql = "INSERT INTO items (name, description, price, vat, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setDouble(4, item.getVat());
            stmt.setInt(5, userId);
            stmt.executeUpdate();
        }
    }

    public static boolean isExist(String name, String description, double price) throws SQLException {
        String sql = "SELECT COUNT(*) FROM items WHERE name = ? AND description = ? AND price = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static ObservableList<Item> getAllItems(int userId) throws SQLException {
        ObservableList<Item> items = FXCollections.observableArrayList();
        String sql = "SELECT * FROM items WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return items;
    }

    public static void updateItem(Item item, int userId) throws SQLException {
        String sql = "UPDATE items SET name = ?, description = ?, price = ?, vat = ? WHERE id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setDouble(4, item.getVat());
            stmt.setInt(5, item.getId());
            stmt.setInt(6, userId);
            stmt.executeUpdate();
        }
    }

    public static void deleteItem(int id, int userId) throws SQLException {
        String sql = "DELETE FROM items WHERE id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }


}
