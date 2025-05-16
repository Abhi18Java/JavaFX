package com.grpcstreamings.resturantapp.dao;

import com.grpcstreamings.resturantapp.model.Item;
import com.grpcstreamings.resturantapp.model.SaleHeader;
import com.grpcstreamings.resturantapp.model.SaleItem;
import com.grpcstreamings.resturantapp.model.Sales;
import com.grpcstreamings.resturantapp.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SalesDAO {
    public static ObservableList<Sales> getAllSales(int userId) throws SQLException {
        ObservableList<Sales> sales = FXCollections.observableArrayList();
        String sql = "SELECT s.*, i.name, i.price, i.vat FROM sales s " +
                "JOIN items i ON s.item_id = i.id " +
                "WHERE s.user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);  // set parameter BEFORE executeQuery

            try (ResultSet rs = stmt.executeQuery()) {
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
        }

        return sales;
    }

    public static void createSale(Sales sales, int userId) throws SQLException {
        String sql = "INSERT INTO sales (item_id, quantity, discount, tip, service_tax, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sales.getItem().getId());
            stmt.setInt(2, sales.getQuantity());
            stmt.setDouble(3, sales.getDiscount());
            stmt.setDouble(4, sales.getTip());
            stmt.setDouble(5, sales.getServiceTax());
            stmt.setInt(6, userId);
            stmt.executeUpdate();
        }
    }

    public static void saveSale(SaleHeader saleHeader) throws SQLException {

        Connection conn = null;
        PreparedStatement headerStmt = null;
        PreparedStatement itemStmt = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            String headerSql = "INSERT INTO sales (sale_date, subtotal, discount, tip, tax, total_amount, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            headerStmt = conn.prepareStatement(headerSql, Statement.RETURN_GENERATED_KEYS);
            headerStmt.setTimestamp(1, Timestamp.valueOf(saleHeader.getSaleDate()));
            headerStmt.setDouble(2, saleHeader.getSubtotal());
            headerStmt.setDouble(3, saleHeader.getDiscount());
            headerStmt.setDouble(4, saleHeader.getTip());
            headerStmt.setDouble(5, saleHeader.getTax());
            headerStmt.setDouble(6, saleHeader.getTotalAmount());
            headerStmt.setInt(7, saleHeader.getUserId());

            int affectedRows = headerStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating sale failed, no rows affected.");
            }

            //Get Generated Sale ID
            int saleId;
            try (ResultSet generatedKeys = headerStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating sale failed, no ID obtained.");
                }
            }
            String itemSql = "INSERT INTO sale_items (sale_id, item_id, quantity, unit_price, line_total) " +
                    "VALUES (?, ?, ?, ?, ?)";

            itemStmt = conn.prepareStatement(itemSql);

            for (SaleItem saleItem : saleHeader.getItems()) {
                itemStmt.setInt(1, saleId);
                itemStmt.setInt(2, saleItem.getItem().getId());
                itemStmt.setInt(3, saleItem.getQuantity());
                itemStmt.setDouble(4, saleItem.getUnitPrice());
                itemStmt.setDouble(5, saleItem.getLineTotal());
                itemStmt.addBatch();
            }

            int[] results = itemStmt.executeBatch();
            for (int result : results) {
                if (result == PreparedStatement.EXECUTE_FAILED) {
                    throw new SQLException("Failed to insert some sale items");
                }
            }
            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        }
    }

    public static ObservableList<SaleHeader> getSalesHistory(int userId) throws SQLException {
        ObservableList<SaleHeader> sales = FXCollections.observableArrayList();

        String sql = "SELECT s.*, GROUP_CONCAT(i.name SEPARATOR ', ') AS items_summary " +
                "FROM sales s " +
                "LEFT JOIN sale_items si ON s.sale_id = si.sale_id " +
                "LEFT JOIN items i ON si.item_id = i.id " +
                "WHERE s.user_id = ? " +
                "GROUP BY s.sale_id " +
                "ORDER BY s.sale_date DESC";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SaleHeader sale = new SaleHeader();
                sale.setSaleId(rs.getInt("sale_id"));
                sale.setSaleDate(rs.getTimestamp("sale_date").toLocalDateTime());
                sale.setSubtotal(rs.getDouble("subtotal"));
                sale.setDiscount(rs.getDouble("discount"));
                sale.setTip(rs.getDouble("tip"));
                sale.setTax(rs.getDouble("tax"));
                sale.setTotalAmount(rs.getDouble("total_amount"));
                sale.setUserId(userId);

                // Load items separately
                sale.setItems(getSaleItems(sale.getSaleId()));
                sales.add(sale);
            }
        }
        return sales;
    }

    private static ObservableList<SaleItem> getSaleItems(int saleId) throws SQLException {
        ObservableList<SaleItem> items = FXCollections.observableArrayList();

        String sql = "SELECT si.*, i.name FROM sale_items si " +
                "JOIN items i ON si.item_id = i.id " +
                "WHERE si.sale_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getDouble("unit_price")
                );
                SaleItem saleItem = new SaleItem(
                        item,
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                );

                items.add(saleItem);
            }
        }
        return items;
    }

    public static void updateSale(SaleHeader saleHeader) throws SQLException {
        Connection conn = null;
        PreparedStatement headerStmt = null;
        PreparedStatement deleteItemsStmt = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            String headerSql = "UPDATE sales SET subtotal=?, discount=?, tip=?, tax=?, total_amount=? WHERE sale_id=?";
            headerStmt = conn.prepareStatement(headerSql);
            headerStmt.setDouble(1, saleHeader.getSubtotal());
            headerStmt.setDouble(2, saleHeader.getDiscount());
            headerStmt.setDouble(3, saleHeader.getTip());
            headerStmt.setDouble(4, saleHeader.getTax());
            headerStmt.setDouble(5, saleHeader.getTotalAmount());
            headerStmt.setInt(6, saleHeader.getSaleId());
            headerStmt.executeUpdate();

            // 2. Delete existing items
            String deleteItemsSql = "DELETE FROM sale_items WHERE sale_id=?";
            deleteItemsStmt = conn.prepareStatement(deleteItemsSql);
            deleteItemsStmt.setInt(1, saleHeader.getSaleId());
            deleteItemsStmt.executeUpdate();

            // 3. Insert new items (same as saveSale)
            try (PreparedStatement itemStmt = conn.prepareStatement(
                    "INSERT INTO sale_items (sale_id, item_id, quantity, unit_price, line_total) VALUES (?,?,?,?,?)")) {

                for (SaleItem item : saleHeader.getItems()) {
                    itemStmt.setInt(1, saleHeader.getSaleId());
                    itemStmt.setInt(2, item.getItem().getId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getUnitPrice());
                    itemStmt.setDouble(5, item.getLineTotal());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

    }

}
