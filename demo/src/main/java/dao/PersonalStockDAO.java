package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.PersonalStock;

public class PersonalStockDAO {
    private static final String INSERT_PERSONAL_STOCK = "INSERT INTO personal_stock (user_id, stock_id, quantity) VALUES (?, ?, ?)";
    private static final String UPDATE_PERSONAL_STOCK = "UPDATE personal_stock SET quantity = ? WHERE user_id = ? AND stock_id = ?";
    private static final String DELETE_PERSONAL_STOCK = "DELETE FROM personal_stock WHERE user_id = ? AND stock_id = ?";
    private static final String GET_PERSONAL_STOCK_BY_USER_AND_STOCK = "SELECT * FROM personal_stock WHERE user_id = ? AND stock_id = ?";
    private static final String GET_ALL_PERSONAL_STOCKS = "SELECT * FROM personal_stock";

    public void addPersonalStock(PersonalStock personalStock) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PERSONAL_STOCK)) {
            preparedStatement.setInt(1, personalStock.getUserId());
            preparedStatement.setInt(2, personalStock.getStockId());
            preparedStatement.setInt(3, personalStock.getQuantity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePersonalStockQuantity(int userId, int stockId, int newQuantity) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PERSONAL_STOCK)) {
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, stockId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePersonalStock(PersonalStock personalStock) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PERSONAL_STOCK)) {
            preparedStatement.setInt(1, personalStock.getQuantity());
            preparedStatement.setInt(2, personalStock.getUserId());
            preparedStatement.setInt(3, personalStock.getStockId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePersonalStock(int userId, int stockId) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PERSONAL_STOCK)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, stockId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PersonalStock getPersonalStockByUserAndStock(int userId, int stockId) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(GET_PERSONAL_STOCK_BY_USER_AND_STOCK)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, stockId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPersonalStockFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PersonalStock> getAllPersonalStocks() {
        List<PersonalStock> personalStocks = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_PERSONAL_STOCKS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                PersonalStock personalStock = extractPersonalStockFromResultSet(resultSet);
                personalStocks.add(personalStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personalStocks;
    }

    private PersonalStock extractPersonalStockFromResultSet(ResultSet resultSet) throws SQLException {
        PersonalStock personalStock = new PersonalStock();
        personalStock.setId(resultSet.getInt("id"));
        personalStock.setUserId(resultSet.getInt("user_id"));
        personalStock.setStockId(resultSet.getInt("stock_id"));
        personalStock.setQuantity(resultSet.getInt("quantity"));
        return personalStock;
    }
}