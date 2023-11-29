package dao;

import model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(User user) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "INSERT INTO user (username, password, balance) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setBigDecimal(3, user.getBalance());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE user SET username = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setInt(2, user.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExists(String username) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password); // Giả sử bạn đã mã hóa mật khẩu trước khi lưu vào cơ sở dữ
                // liệu
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUsername(String username) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setUsername(resultSet.getString("username"));
                        // Kiểm tra xem cột "password" có tồn tại hay không
                        if (resultSet.getString("password") != null) {
                            // Mật khẩu được lấy từ cơ sở dữ liệu và được mã hóa trước khi gán
                            String hashedPassword = resultSet.getString("password");
                            user.setPassword(hashedPassword);
                        } else {
                            // Xử lý nếu cột "password" không tồn tại
                            // Có thể thông báo hoặc xử lý theo cách khác tùy vào yêu cầu
                            System.out.println("Column 'password' does not exist for user " + username);
                            return null;
                        }

                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            // Xử lý lỗi SQL, có thể thông báo hoặc ghi log
            e.printStackTrace();
        }
        return null;
    }

    public String getPasswordByUsername(String username) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT password FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy mật khẩu
    }

    public int getUserIdByUsername(String username) {
        // Giả sử bạn có một câu truy vấn SQL hoặc sử dụng JDBC để truy vấn cơ sở dữ liệu
        // Đây là một ví dụ đơn giản, bạn cần thay đổi dựa trên cơ sở dữ liệu của bạn

        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT id FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ theo ý muốn của bạn
        }

        return -1; // Trả về giá trị mặc định nếu không tìm thấy UserId
    }

    public BigDecimal getUserBalanceById(int userId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT balance FROM user WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBigDecimal("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error-handling strategy
        }
        return BigDecimal.ZERO; // Return a default value if the balance is not found or an error occurs
    }

    public void updateUserBalance(int userId, BigDecimal newBalance) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "UPDATE user SET balance = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBigDecimal(1, newBalance);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getUserBalance(String userName) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT balance FROM user WHERE userName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBigDecimal("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO; // Trả về giá trị mặc định nếu không tìm thấy số dư
    }

    public User getUserById(int userId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM user WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setUsername(resultSet.getString("username"));

                        // Kiểm tra xem cột "password" có tồn tại hay không
                        if (resultSet.getString("password") != null) {
                            // Mật khẩu được lấy từ cơ sở dữ liệu và được mã hóa trước khi gán
                            String hashedPassword = resultSet.getString("password");
                            user.setPassword(hashedPassword);
                        } else {
                            // Xử lý nếu cột "password" không tồn tại
                            // Có thể thông báo hoặc xử lý theo cách khác tùy vào yêu cầu
                            System.out.println("Column 'password' does not exist for user with ID " + userId);
                            return null;
                        }

                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            // Xử lý lỗi SQL, có thể thông báo hoặc ghi log
            e.printStackTrace();
        }
        return null;
    }
}