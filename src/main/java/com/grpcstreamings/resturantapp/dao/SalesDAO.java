package com.grpcstreamings.resturantapp.dao;

import com.grpcstreamings.resturantapp.model.Item;
import com.grpcstreamings.resturantapp.model.Sales;
import com.grpcstreamings.resturantapp.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesDAO {
    public static ObservableList<Sales> getAllSales() throws SQLException {
        ObservableList<Sales> sales = FXCollections.observableArrayList();
        String sql = "SELECT s.*, i.name, i.price, i.vat FROM sales s " +
                "JOIN items i ON s.item_id = i.id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        "", // description
                        rs.getDouble("price"),
                        rs.getDouble("vat")
                );

                Sales sale = new Sales(
                        rs.getInt("id"),
                        item,
                        rs.getInt("quantity"),
                        rs.getDouble("discount"),
                        rs.getDouble("tip"),
                        rs.getDouble("service_tax"),
                        rs.getTimestamp("sale_date").toLocalDateTime()
                );
                sales.add(sale);
            }
        }
        return sales;
    }

    public static void createSale(Sales sales) throws SQLException {
        String sql = "INSERT INTO sales (item_id, quantity, discount, tip, service_tax) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sales.getItem().getId());
            stmt.setInt(2, sales.getQuantity());
            stmt.setDouble(3, sales.getDiscount());
            stmt.setDouble(4, sales.getTip());
            stmt.setDouble(5, sales.getServiceTax());
            stmt.executeUpdate();
        }
    }

}
