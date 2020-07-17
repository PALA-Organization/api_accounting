package fr.pala.accounting.user.infrastructure.controller;

import fr.pala.accounting.user.service.exception.UserAlreadyExistsException;
import fr.pala.accounting.user.service.CreateUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    private final CreateUser createUser;


    public UserController(CreateUser createUser) {
        this.createUser = createUser;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Valid @RequestBody UserDTO userDTO) {
        try{
            createUser.createUser(userDTO);
        }
        catch (UserAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
