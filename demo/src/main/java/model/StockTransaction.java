package model;

import java.math.BigDecimal;
import java.sql.Date;

public class StockTransaction {
    private int id;
    private int stockId;
    private int userId;
    private TransactionType transactionType;
    private Date transactionDate;
    private int quantity;
    private BigDecimal price;

    public enum TransactionType {
        BUY,
        SELL,
        LIMIT_BUY,
        LIMIT_SELL
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", userId=" + userId +
                ", transactionType=" + transactionType +
                ", transactionDate=" + transactionDate +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}