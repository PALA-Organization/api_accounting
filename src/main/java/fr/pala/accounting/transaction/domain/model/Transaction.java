package fr.pala.accounting.transaction.domain.model;

import java.util.Date;

public class Transaction {

    private String id;
    private String type;
    private String shop_name;
    private String shop_address;
    private Date date;
    private Double amount;
    private String description;

    public Transaction(String id, String type, String shop_name, String shop_address, Date date, Double amount, String description){
        this.id = id;
        this.type = type;
        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
