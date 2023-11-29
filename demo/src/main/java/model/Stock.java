package model;

public class Stock {
    private int id;
    private String symbol;
    private String company;

    public Stock() {

    }

    public Stock(int id, String symbol, String company) {
        this.id = id;
        this.symbol = symbol;
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}