package controller;

import dao.PersonalStockDAO;
import dao.StockDAO;
import dao.StockPriceDAO;
import dao.StockTransactionDAO;
import dao.UserDAO;
import model.PersonalStock;
import model.Stock;
import model.StockPrice;
import model.StockTransaction;
import model.StockTransaction.TransactionType;
import model.User;

import java.math.BigDecimal;
import java.util.List;

public class StockController {
    private final StockDAO stockDAO;
    private final StockTransactionDAO stockTransactionDAO;
    private final UserDAO userDAO;

    private final PersonalStockDAO personalStockDAO;

    public StockController(StockDAO stockDAO, StockTransactionDAO stockTransactionDAO, UserDAO userDAO, PersonalStockDAO personalStockDAO) {
        this.stockDAO = stockDAO;
        this.stockTransactionDAO = stockTransactionDAO;
        this.userDAO = userDAO;
        this.personalStockDAO = personalStockDAO;
    }

    public List<Stock> getAllStocks() {
        return stockDAO.getAllStocks();
    }

    public List<StockPrice> getAllStockPrices() {
        return stockDAO.getAllStockPrices();
    }

    public BigDecimal getUserBalance(int userId) {
        return userDAO.getUserBalanceById(userId);
    }

    public List<PersonalStock> getAllPersonalStock() {
        return personalStockDAO.getAllPersonalStocks();
    }
    private BigDecimal getCurrentPriceForStock(int stockId, List<StockPrice> stockPrices) {
        for (StockPrice stockPrice : stockPrices) {
            if (stockPrice.getStockId() == stockId) {
                return stockPrice.getPrice();
            }
        }
        return BigDecimal.ZERO; // Trả về giá mặc định hoặc xử lý theo nhu cầu
    }

    public void addPersonalStock(int userId, int stockId, int quantity) {
        // Thực hiện giao dịch mua ở đây (cập nhật bảng transaction và stock, kiểm tra số dư v.v.)

        // Sau khi giao dịch thành công, thêm thông tin vào PersonalStock
        PersonalStock personalStock = new PersonalStock();
        personalStock.setUserId(userId);
        personalStock.setStockId(stockId);
        personalStock.setQuantity(quantity);

        personalStockDAO.addPersonalStock(personalStock);
    }


    public void viewAllStockDetails() {
        List<Stock> stocks = stockDAO.getAllStocks();
        StockPriceDAO stockPriceDAO = new StockPriceDAO();

        if (stocks.isEmpty()) {
            System.out.println("Không có cổ phiếu khả dụng.");
        } else {
            System.out.println("Thông tin tất cả cổ phiếu:");
            for (Stock stock : stocks) {
                System.out.println("ID Cổ phiếu: " + stock.getId());
                System.out.println("Ký hiệu: " + stock.getSymbol());
                System.out.println("Công ty: " + stock.getCompany());

                List<StockPrice> stockPrices = stockPriceDAO.getStockPricesByStockId(stock.getId());
                if (!stockPrices.isEmpty()) {
                    BigDecimal currentPrice = getCurrentPriceForStock(stock.getId(), stockPrices);
                    System.out.println("Giá hiện tại: " + (currentPrice != null ? currentPrice : "N/A"));
                    // Hiển thị các thông tin khác nếu cần
                } else {
                    System.out.println("Không có thông tin giá cho cổ phiếu này.");
                }

                System.out.println("----------------------------------");
            }
        }
    }

    public List<StockTransaction> getTransactionHistory() {
        return stockTransactionDAO.getAllStockTransactions();
    }

    public void buyStock(int userId, int stockId, int quantity, BigDecimal userBalance) {
        performTransaction(userId, stockId, quantity, userBalance, "BUY");
    }

    public void sellStock(int userId, int stockId, int quantity, BigDecimal userBalance) {
        performTransaction(userId, stockId, quantity, userBalance, "SELL");
    }

    public void performTransaction(int userId, int stockId, int quantity, BigDecimal userBalance,
                                   String transactionType) {
        StockPriceDAO stockPriceDAO = new StockPriceDAO();
        List<StockPrice> stockPrices = stockPriceDAO.getAllStockPrices();
        PersonalStockDAO personalStockDAO = new PersonalStockDAO();
        Stock stock = stockDAO.getStockById(stockId);
        User user = userDAO.getUserById(userId);
        user.setBalance(userBalance);
        if (stock == null || user == null) {
            System.out.println("Không tìm thấy cổ phiếu hoặc người dùng.");
            return;
        }
        BigDecimal stockPrice = getCurrentPriceForStock(stock.getId(), stockPrices);
        BigDecimal currentPrice = stockPrice.multiply(BigDecimal.valueOf(quantity));
        if ((transactionType.equals("BUY") && userBalance != null && userBalance.compareTo(currentPrice) < 0)
                || (transactionType.equals("SELL")
                && quantity > personalStockDAO.getPersonalStockByUserAndStock(userId, stockId).getQuantity())
                || transactionType.equals("BUY") && quantity > stockPriceDAO.getStockPriceById(stockId).getQuantity()) {
            System.out.println("Giao dịch không thể hoàn thành. Kiểm tra số dư hoặc số lượng cổ phiếu.");
            return;
        }

        // Thực hiện giao dịch
        StockTransaction transaction = createTransaction(userId, stockId, quantity, userBalance, transactionType);
        stockTransactionDAO.addStockTransaction(transaction);
        System.out.println("User Balance Before Transaction: " + user.getBalance());
        updateBalance(user, transaction);
        updateStockPrice(stockPriceDAO.getStockPriceById(stock.getId()), transaction);
        updatePersonalStock(userId, stockId, quantity, transactionType); // Thêm dòng này
        System.out.println("User Balance After Transaction: " + user.getBalance());
        System.out.println("Giao dịch thành công. ID giao dịch: " + transaction.getId());
    }

    private StockTransaction createTransaction(int userId, int stockId, int quantity, BigDecimal userBalance,
                                               String transactionType) {
        Stock stock = stockDAO.getStockById(stockId);
        StockPriceDAO stockPriceDAO = new StockPriceDAO();
        List<StockPrice> stockPrices = stockPriceDAO.getAllStockPrices();
        BigDecimal currentPrice = getCurrentPriceForStock(stock.getId(), stockPrices);

        StockTransaction transaction = new StockTransaction();
        transaction.setStockId(stockId);
        transaction.setUserId(userId);
        transaction.setTransactionType(StockTransaction.TransactionType.valueOf(transactionType.toUpperCase()));
        transaction.setTransactionDate(DateUtils.getCurrentDate());
        transaction.setQuantity(quantity);
        BigDecimal resultPrice = currentPrice.multiply(BigDecimal.valueOf(quantity));
        transaction.setPrice(resultPrice);
        return transaction;
    }

    private void updateBalance(User user, StockTransaction transaction) {
        if (user != null) {

            System.out.println("Transaction Amount: " + transaction.getPrice());

            if (transaction.getTransactionType() == StockTransaction.TransactionType.BUY) {
                user.setBalance(user.getBalance().subtract(transaction.getPrice()));
            } else if (transaction.getTransactionType() == StockTransaction.TransactionType.SELL) {
                user.setBalance(user.getBalance().add(transaction.getPrice()));
            }

            userDAO.updateUserBalance(user.getId(), user.getBalance());
        }
    }

    private void updateStockPrice(StockPrice stockprice, StockTransaction transaction) {
        int newQuantity;
        if (stockprice != null) {
            // Giảm hoặc tăng tuỳ BUY hoặc SELL quantity trong bảng stock_price sau khi mua
            // thành công
            if (transaction.getTransactionType().equals(TransactionType.BUY)) {
                newQuantity = stockprice.getQuantity() - transaction.getQuantity();
            } else {
                newQuantity = stockprice.getQuantity() + transaction.getQuantity();
            }
            stockprice.setQuantity(newQuantity);

            // Tạo một đối tượng StockPriceDAO và gọi phương thức updateStockPrice
            StockPriceDAO stockPriceDAO = new StockPriceDAO();
            stockPriceDAO.updateStockPrice(stockprice);
        }
    }

    private void updatePersonalStock(int userId, int stockId, int quantity, String transactionType) {
        PersonalStockDAO personalStockDAO = new PersonalStockDAO();

        // Lấy thông tin PersonalStock hiện tại (nếu có)
        PersonalStock existingPersonalStock = personalStockDAO.getPersonalStockByUserAndStock(userId, stockId);

        if (existingPersonalStock == null) {
            // Nếu không có, tạo mới và đặt giá trị quantity dựa trên BUY hoặc SELL
            PersonalStock personalStock = new PersonalStock();
            personalStock.setUserId(userId);
            personalStock.setStockId(stockId);
            personalStock.setQuantity(
                    transactionType.equals("BUY") ? quantity : 0);

            // Thêm mới PersonalStock
            personalStockDAO.addPersonalStock(personalStock);
        } else {
            // Nếu có, cập nhật giá trị quantity dựa trên BUY hoặc SELL
            existingPersonalStock.setQuantity(
                    transactionType.equals("BUY") ? existingPersonalStock.getQuantity() + quantity
                            : existingPersonalStock.getQuantity() - quantity);

            // Cập nhật PersonalStock
            personalStockDAO.updatePersonalStock(existingPersonalStock);
        }
    }
}