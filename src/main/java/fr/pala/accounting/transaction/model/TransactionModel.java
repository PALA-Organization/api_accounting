package fr.pala.accounting.transaction.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Transaction")
@Data
@Accessors(chain = true)
public class TransactionModel {

    @Id
    private String transaction_id;
    private String type;
    private String shop_name;
    private String shop_address;
    private Date date;
    private Double amount;
    private String description;

    public TransactionModel(String transaction_id, String type, String shop_name, String shop_address, Date date, Double amount, String description){
        this.transaction_id = transaction_id;
        this.type = type;
        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
    public TransactionModel(){}

}
