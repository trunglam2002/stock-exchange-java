package view;

import controller.StockController;
import controller.UserController;
import dao.StockDAO;
import dao.StockTransactionDAO;
import dao.UserDAO;
import model.PersonalStock;
import model.Stock;
import model.StockPrice;
import model.StockTransaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class StockAppView {
    private StockController stockController;
    private JFrame frame;
    private JTable stockTable, transactionTable;
    private DefaultTableModel stockTableModel, transactionTableModel;
    private int userId;
    private JLabel balanceLabel;
    private UserDAO userDAO = new UserDAO();

    public int getUserId() {
        return userId;
    }

    public StockAppView(StockController stockController, int userId) {
        this.stockController = stockController;
        this.userId = userId;

        frame = new JFrame("Stock App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));  // Tăng số hàng để chứa label số dư
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        JLabel userIdLabel = new JLabel("User ID: " + userId); // Tạo label để hiển thị UserId
        panel.add(userIdLabel);

        balanceLabel = new JLabel("Balance: $" + stockController.getUserBalance(userId));
        panel.add(balanceLabel);

        JButton viewPersonalStockButton = new JButton("View Personal Stocks");
        viewPersonalStockButton.addActionListener(e -> handleViewPersonalStock());
        panel.add(viewPersonalStockButton);

        JButton viewStocksButton = new JButton("View All Stocks");
        viewStocksButton.addActionListener(e -> handleViewStocks());
        panel.add(viewStocksButton);

        JButton viewTransactionHistoryButton = new JButton("View Transaction History");
        viewTransactionHistoryButton.addActionListener(e -> handleViewTransactionHistory());
        panel.add(viewTransactionHistoryButton);

        JButton buyStockButton = new JButton("Buy Stock");
        buyStockButton.addActionListener(e -> handleBuyStock());
        panel.add(buyStockButton);

        JButton sellStockButton = new JButton("Sell Stock");
        sellStockButton.addActionListener(e -> handleSellStock());
        panel.add(sellStockButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        panel.add(logoutButton);
    }

    private void handleViewStocks() {
        List<Stock> stocks = stockController.getAllStocks();
        List<StockPrice> stockPrices = stockController.getAllStockPrices();

        if (stocks.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No stocks available.");
        } else {
            showAllStocksWindow(stocks, stockPrices);
        }
    }


    private void showAllStocksWindow(List<Stock> stocks, List<StockPrice> stockPrices) {
        JFrame stocksFrame = new JFrame("All Stocks");
        stocksFrame.setSize(600, 400);

        DefaultTableModel stockTableModel = new DefaultTableModel();
        stockTableModel.addColumn("ID");
        stockTableModel.addColumn("Symbol");
        stockTableModel.addColumn("Company");
        stockTableModel.addColumn("Price");
        stockTableModel.addColumn("Quantity");

        // Thêm dữ liệu vào bảng
        for (Stock stock : stocks) {
            StockPrice stockPrice = findStockPrice(stockPrices, stock.getId());
            stockTableModel.addRow(new Object[]{
                    stock.getId(),
                    stock.getSymbol(),
                    stock.getCompany(),
                    stockPrice.getPrice(),
                    stockPrice.getQuantity()
            });
        }

        JTable stockTable = new JTable(stockTableModel);

        JScrollPane scrollPane = new JScrollPane(stockTable);
        stocksFrame.add(scrollPane);

        stocksFrame.setVisible(true);
    }

    private StockPrice findStockPrice(List<StockPrice> stockPrices, int stockId) {
        for (StockPrice stockPrice : stockPrices) {
            if (stockPrice.getStockId() == stockId) {
                return stockPrice;
            }
        }
        return new StockPrice(); // Trả về đối tượng StockPrice mặc định nếu không tìm thấy
    }

    private void handleViewPersonalStock() {
        List<PersonalStock> personalStocks = stockController.getAllPersonalStock();

        if (personalStocks.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Không có cổ phiếu cá nhân.");
        } else {
            showPersonalStockWindow(personalStocks);
        }
    }

    private void showPersonalStockWindow(List<PersonalStock> personalStocks) {
        JFrame personalStockFrame = new JFrame("Personal Stock");
        personalStockFrame.setSize(500, 300);

        DefaultTableModel personalStockTableModel = new DefaultTableModel();
        personalStockTableModel.addColumn("ID");
        personalStockTableModel.addColumn("Stock ID");
        personalStockTableModel.addColumn("Quantity");

        JTable personalStockTable = new JTable(personalStockTableModel);

        // Thêm dữ liệu vào bảng
        for (PersonalStock personalStock : personalStocks) {
            personalStockTableModel.addRow(new Object[]{
                    personalStock.getId(),
                    personalStock.getStockId(),
                    personalStock.getQuantity()
            });
        }

        JScrollPane scrollPane = new JScrollPane(personalStockTable);
        personalStockFrame.add(scrollPane);

        personalStockFrame.setVisible(true);
    }


    private void handleViewTransactionHistory() {
        try {
            int userId = getUserFromUserId();

            // Lấy thông tin lịch sử giao dịch từ controller
            List<StockTransaction> transactions = stockController.getTransactionHistory();

            // Kiểm tra xem có dữ liệu để hiển thị hay không
            if (transactions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No transaction history available.");
            } else {
                // Hiển thị cửa sổ mới chứa bảng lịch sử giao dịch
                showTransactionHistoryWindow(transactions);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid user ID.");
        }
    }

    private void showTransactionHistoryWindow(List<StockTransaction> transactions) {
        JFrame transactionFrame = new JFrame("Transaction History");
        transactionFrame.setSize(500, 300);

        DefaultTableModel transactionTableModel = new DefaultTableModel();
        transactionTableModel.addColumn("ID");
        transactionTableModel.addColumn("Type");
        transactionTableModel.addColumn("Date");
        transactionTableModel.addColumn("Quantity");
        transactionTableModel.addColumn("Price");

        JTable transactionTable = new JTable(transactionTableModel);

        // Thêm dữ liệu vào bảng
        for (StockTransaction transaction : transactions) {
            transactionTableModel.addRow(new Object[]{
                    transaction.getId(),
                    transaction.getTransactionType(),
                    transaction.getTransactionDate(),
                    transaction.getQuantity(),
                    transaction.getPrice()
            });
        }

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        transactionFrame.add(scrollPane);

        transactionFrame.setVisible(true);
    }

    // Inside StockAppView class
    private void handleBuyStock() {
        try {
            int userID = getUserFromUserId();
            int stockId = getStockIdFromUserInput("Enter Stock ID for buying:");
            int quantity = getQuantityFromUserInput("Enter quantity for buying:");
            BigDecimal userBalance = getUserBalanceFromUserInput();

            stockController.buyStock(userID, stockId, quantity, userBalance);
            JOptionPane.showMessageDialog(frame, "Stock bought successfully!");
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Balance: $" + stockController.getUserBalance(userId));  // Cập nhật nội dung của label số dư
    }

    private void handleSellStock() {
        try {
            int userId = getUserFromUserId();
            int stockId = getStockIdFromUserInput("Enter Stock ID for selling:");
            int quantity = getQuantityFromUserInput("Enter quantity for selling:");
            BigDecimal userBalance = getUserBalanceFromUserInput();

            stockController.sellStock(userId, stockId, quantity, userBalance);
            JOptionPane.showMessageDialog(frame, "Stock sold successfully!");
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
        }
    }

    private int getUserFromUserId() {
        return userId;
    }

    private int getStockIdFromUserInput(String message) {
        return Integer.parseInt(JOptionPane.showInputDialog(frame, message));
    }

    private int getQuantityFromUserInput(String message) {
        return Integer.parseInt(JOptionPane.showInputDialog(frame, message));
    }

    private BigDecimal getUserBalanceFromUserInput() {
        return userDAO.getUserBalanceById(userId);
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            // Đóng cửa sổ hiện tại
            frame.dispose();

            // Hiển thị cửa sổ đăng nhập mới
            new UserRegistrationLoginView(new UserController(new UserDAO()));
        }
    }
}