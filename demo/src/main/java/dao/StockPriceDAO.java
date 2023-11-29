package dao;

import model.StockPrice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockPriceDAO {
    public List<StockPrice> getStockPricesByStockId(int stockId) {
        List<StockPrice> stockPrices = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock_price WHERE stock_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        StockPrice stockPrice = new StockPrice();
                        stockPrice.setId(resultSet.getInt("id"));
                        stockPrice.setStockId(resultSet.getInt("stock_id"));
                        stockPrice.setQuantity(resultSet.getInt("quantity"));
                        stockPrice.setPrice(resultSet.getBigDecimal("price"));
                        stockPrices.add(stockPrice);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockPrices;
    }

    public StockPrice getStockPriceById(int stockPriceId) {
        StockPrice stockPrice = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock_price WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockPriceId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        stockPrice = new StockPrice();
                        stockPrice.setId(resultSet.getInt("id"));
                        stockPrice.setStockId(resultSet.getInt("stock_id"));
                        stockPrice.setQuantity(resultSet.getInt("quantity"));
                        stockPrice.setPrice(resultSet.getBigDecimal("price"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockPrice;
    }

    public void addStockPrice(StockPrice stockPrice) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "INSERT INTO stock_price (stock_id, quantity, price) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, stockPrice.getStockId());
                preparedStatement.setInt(2, stockPrice.getQuantity());
                preparedStatement.setBigDecimal(3, stockPrice.getPrice());
                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        stockPrice.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStockPrice(StockPrice stockPrice) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE stock_price SET quantity = ?, price = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockPrice.getQuantity());
                preparedStatement.setBigDecimal(2, stockPrice.getPrice());
                preparedStatement.setInt(3, stockPrice.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStockPrice(int stockPriceId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "DELETE FROM stock_price WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, stockPriceId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StockPrice> getAllStockPrices() {
        List<StockPrice> stockPrices = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM stock_price";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        StockPrice stockPrice = new StockPrice();
                        stockPrice.setId(resultSet.getInt("id"));
                        stockPrice.setStockId(resultSet.getInt("stock_id"));
                        stockPrice.setQuantity(resultSet.getInt("quantity"));
                        stockPrice.setPrice(resultSet.getBigDecimal("price"));
                        stockPrices.add(stockPrice);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockPrices;
    }
}