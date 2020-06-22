package fr.pala.accounting.transaction.domain.model;

import java.util.Date;

public class Transaction {

    private final String id;
    private final String type;
    private final String shop_name;
    private final String shop_address;
    private final Date date;
    private final Double amount;
    private final String description;

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

    public String getType() {
        return type;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public Date getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
