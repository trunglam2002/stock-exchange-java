package model;

public class PersonalStock {
    private int id;
    private int userId;
    private int stockId;
    private int quantity;

    // Constructors, getters, setters

    public PersonalStock() {
        // Constructor mặc định
    }

    public PersonalStock(int id, int userId, int stockId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PersonalStock{" +
                "id=" + id +
                ", userId=" + userId +
                ", stockId=" + stockId +
                ", quantity=" + quantity +
                '}';
    }
}