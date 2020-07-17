package fr.pala.accounting.transaction.infrastructure.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Transaction")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionMongoModel {

    @Id
    private String id;
    private String type;
    private String shop_name;
    private String shop_address;
    private Date date;
    private Double amount;
    private String description;
}
