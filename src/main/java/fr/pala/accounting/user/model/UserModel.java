package fr.pala.accounting.user.model;

import fr.pala.accounting.account.infrastructure.dao.AccountModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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
    private String email;
    private String password;

    private Date created;
    private Date last_connection;
    private List<AccountModel> accounts;

    public UserModel(String user_id, String name, String email, String password, Date created, Date last_connection, List<AccountModel> accounts){
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.last_connection = last_connection;
        this.accounts = accounts;
    }

    public UserDTO toDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(this.getEmail());
        userDTO.setName(this.getName());
        userDTO.setPassword(this.getPassword());
        userDTO.setUser_id(this.getUser_id());
        return userDTO;
    }
}

