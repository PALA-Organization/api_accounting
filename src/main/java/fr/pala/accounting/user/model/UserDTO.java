package fr.pala.accounting.user.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class UserDTO {

    private String user_id;
    @NotEmpty(message = "Name should not be null")
    private String name;
    @NotEmpty(message = "Email should not be null")
    @Email(message = "Email is not valid")
    private String email;
    @NotEmpty(message = "Password should not be null")
    private String password;
}
