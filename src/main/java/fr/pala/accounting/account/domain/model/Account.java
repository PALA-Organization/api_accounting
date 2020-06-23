package fr.pala.accounting.account.domain.model;

import java.util.List;

public class Account {

    private String id;
    private Double amount;
    private List<String> transactions_ids;

    public Account(String id, Double amount, List<String> transactions_ids) throws InvalidFieldException {
        this.id = id;
        this.amount = amount;
        this.transactions_ids = transactions_ids;

        this.canBeSaved();
    }

    private Boolean canBeSaved() throws InvalidFieldException {
        if (amount == null || transactions_ids == null) {
            throw new InvalidFieldException();
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<String> getTransactions_ids() {
        return transactions_ids;
    }

    public void setTransactions_ids(List<String> transactions_ids) {
        this.transactions_ids = transactions_ids;
    }

    public void updateAmount(Double amount) {
        setAmount(getAmount() + amount);
    }
}
