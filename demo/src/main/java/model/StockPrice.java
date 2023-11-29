package model;

import java.math.BigDecimal;

public class StockPrice {
    private int id;
    private int stockId;
    private int quantity;
    private BigDecimal price;

    // Constructors, getters, setters
    public StockPrice() {

    }

    public StockPrice(int id, int stockId, int quantity, BigDecimal price) {
        this.id = id;
        this.stockId = stockId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getStockId() {
        return stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}