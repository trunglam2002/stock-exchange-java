package controller;

import dao.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public boolean validateLogin(String username, String password) {
        // Kiểm tra xem username có tồn tại trong cơ sở dữ liệu hay không
        int userId = userDAO.getUserIdByUsername(username);

        if (userId != -1) {
            // Nếu userId khác -1, tức là username tồn tại
            // Lấy mật khẩu đã lưu trong cơ sở dữ liệu
            String storedPasswordHash = userDAO.getPasswordByUsername(username);

            if (storedPasswordHash != null && BCrypt.checkpw(password, storedPasswordHash)) {
                // Mật khẩu đúng, đăng nhập thành công
                return true;
            }
        }

        // Đăng nhập thất bại
        return false;
    }

    public int getUserIdByUserName(String username) {
        return userDAO.getUserIdByUsername(username);
    }

    public void updateBalance(int userId, BigDecimal amount) {
        try {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                user.setBalance(user.getBalance().add(amount));
                userDAO.updateUser(user);
            }
        } catch (Exception e) {
            logger.error("Error updating balance for user with ID {}", userId, e);
        }
    }

    public boolean registerUser(String username, String password, BigDecimal initialBalance) {
        try {
            // Kiểm tra xem người dùng đã tồn tại chưa
            if (userDAO.isUserExists(username)) {
                logger.warn("Username '{}' already exists. Please choose another one.", username);
                return false;
            }

            // Nếu chưa tồn tại, thêm người dùng mới vào cơ sở dữ liệu với số dư khởi đầu
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashedPassword);
            newUser.setBalance(initialBalance);
            userDAO.addUser(newUser);
            logger.info("Registration successful for user '{}'", username);
            return true;
        } catch (Exception e) {
            logger.error("Error registering user '{}'", username, e);
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            // Lấy thông tin người dùng từ cơ sở dữ liệu
            User user = userDAO.getUserByUsername(username);

            // Kiểm tra xem người dùng có tồn tại và mật khẩu đúng không
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                logger.info("Login successful for user '{}'", username);
                // Cập nhật số dư nếu cần thiết
                updateBalance(user.getId(), BigDecimal.ZERO); // Số dư có thể được cập nhật dựa trên logic cụ thể
                return true;
            } else {
                logger.warn("Invalid username or password for user '{}'", username);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error during login for user '{}'", username, e);
            return false;
        }
    }
}
