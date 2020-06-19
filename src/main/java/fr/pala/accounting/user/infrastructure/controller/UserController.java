package fr.pala.accounting.user.infrastructure.controller;

import fr.pala.accounting.user.domain.exception.UserAlreadyExistsException;
import fr.pala.accounting.user.infrastructure.dao.UserDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDAO userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder, UserDAO userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Valid @RequestBody UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        try{
            userService.addUser(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
        }
        catch (UserAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
