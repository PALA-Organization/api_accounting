package fr.pala.accounting.account.infrastructure.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Account")
@Getter
@Setter
public class AccountModel {

    @Id
    private String id;
    private Double amount;
    private List<String> transactions_ids;

    public AccountModel(String id, Double amount, List<String> transactions_ids){
        this.id = id;
        this.amount = amount;
        this.transactions_ids = transactions_ids;
    }
}