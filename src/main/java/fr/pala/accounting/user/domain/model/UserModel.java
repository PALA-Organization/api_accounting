package fr.pala.accounting.user.domain.model;

import fr.pala.accounting.account.infrastructure.dao.AccountMongoModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "User")
@Getter
@Setter
public class UserModel {

    @Id
    private String user_id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;

    private Date created;
    private Date last_connection;
    private List<AccountMongoModel> accounts;

    public UserModel(String user_id, String name, String email, String password, Date created, Date last_connection, List<AccountMongoModel> accounts){
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.last_connection = last_connection;
        this.accounts = accounts;
    }
}

