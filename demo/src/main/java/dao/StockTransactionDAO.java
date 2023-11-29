package dao;

import model.StockTransaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockTransactionDAO {

    public void addStockTransaction(StockTransaction stockTransaction) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "INSERT INTO stock_transaction (stock_id, user_id, transaction_type, transaction_date, quantity, price) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockTransaction.getStockId());
                preparedStatement.setInt(2, stockTransaction.getUserId());
                preparedStatement.setString(3, String.valueOf(stockTransaction.getTransactionType()));
                preparedStatement.setDate(4, stockTransaction.getTransactionDate());
                preparedStatement.setInt(5, stockTransaction.getQuantity());
                preparedStatement.setBigDecimal(6, stockTransaction.getPrice());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StockTransaction> getAllStockTransactions() {
        List<StockTransaction> stockTransactions = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock_transaction";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        StockTransaction stockTransaction = new StockTransaction();
                        stockTransaction.setId(resultSet.getInt("id"));
                        stockTransaction.setStockId(resultSet.getInt("stock_id"));
                        stockTransaction.setUserId(resultSet.getInt("user_id"));
                        stockTransaction.setTransactionType(
                                StockTransaction.TransactionType.valueOf(resultSet.getString("transaction_type")));
                        stockTransaction.setTransactionDate(resultSet.getDate("transaction_date"));
                        stockTransaction.setQuantity(resultSet.getInt("quantity"));
                        stockTransaction.setPrice(BigDecimal.valueOf(resultSet.getDouble("price")));
                        stockTransactions.add(stockTransaction);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockTransactions;
    }

    public void updateStockTransaction(StockTransaction stockTransaction) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE stock_transaction SET stock_id = ?, user_id = ?, transaction_type = ?, " +
                    "transaction_date = ?, quantity = ?, price = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockTransaction.getStockId());
                preparedStatement.setInt(2, stockTransaction.getUserId());
                preparedStatement.setString(3, String.valueOf(stockTransaction.getTransactionType()));
                preparedStatement.setDate(4, stockTransaction.getTransactionDate());
                preparedStatement.setInt(5, stockTransaction.getQuantity());
                preparedStatement.setBigDecimal(6, stockTransaction.getPrice());
                preparedStatement.setInt(7, stockTransaction.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StockTransaction> getStockTransactionsByUser(int userId) {
        List<StockTransaction> stockTransactions = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock_transaction WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        StockTransaction stockTransaction = new StockTransaction();
                        stockTransaction.setId(resultSet.getInt("id"));
                        stockTransaction.setStockId(resultSet.getInt("stock_id"));
                        stockTransaction.setUserId(resultSet.getInt("user_id"));
                        stockTransaction.setTransactionType(
                                StockTransaction.TransactionType.valueOf(resultSet.getString("transaction_type")));
                        stockTransaction.setTransactionDate(resultSet.getDate("transaction_date"));
                        stockTransaction.setQuantity(resultSet.getInt("quantity"));
                        stockTransaction.setPrice(BigDecimal.valueOf(resultSet.getDouble("price")));
                        stockTransactions.add(stockTransaction);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockTransactions;
    }

    public List<StockTransaction> getStockTransactionsByStock(int stockId) {
        List<StockTransaction> stockTransactions = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock_transaction WHERE stock_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        StockTransaction stockTransaction = new StockTransaction();
                        stockTransaction.setId(resultSet.getInt("id"));
                        stockTransaction.setStockId(resultSet.getInt("stock_id"));
                        stockTransaction.setUserId(resultSet.getInt("user_id"));
                        stockTransaction.setTransactionType(
                                StockTransaction.TransactionType.valueOf(resultSet.getString("transaction_type")));
                        stockTransaction.setTransactionDate(resultSet.getDate("transaction_date"));
                        stockTransaction.setQuantity(resultSet.getInt("quantity"));
                        stockTransaction.setPrice(BigDecimal.valueOf(resultSet.getDouble("price")));
                        stockTransactions.add(stockTransaction);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockTransactions;
    }

    public void deleteStockTransaction(int stockTransactionId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "DELETE FROM stock_transaction WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockTransactionId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}