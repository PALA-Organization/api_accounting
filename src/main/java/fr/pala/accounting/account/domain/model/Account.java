package fr.pala.accounting.account.domain.model;

import java.util.List;

public class Account {

    private final String id;
    private final Double amount;
    private final List<String> transactions_ids;

    public Account(String id, Double amount, List<String> transactions_ids) throws InvalidFieldException {
        this.id = id;
        this.amount = amount;
        this.transactions_ids = transactions_ids;

        this.canBeSaved();
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public List<String> getTransactions_ids() {
        return transactions_ids;
    }

    private Boolean canBeSaved() throws InvalidFieldException {
        if (amount == null || transactions_ids == null) {
            throw new InvalidFieldException();
        }
        return true;
    }
}
