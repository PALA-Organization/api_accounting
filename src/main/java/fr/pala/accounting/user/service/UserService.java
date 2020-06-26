package fr.pala.accounting.user.service;

import fr.pala.accounting.user.infrastructure.dao.UserDAO;
import fr.pala.accounting.user.infrastructure.controller.UserDTO;
import fr.pala.accounting.user.domain.model.UserModel;
import fr.pala.accounting.user.service.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserDAO userDAO) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
    }

    public UserModel createUser(UserDTO userDTO){
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDAO.getUserByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException(
                    "There is an user with that email address : " + userDTO.getEmail());
        }
        UserModel user = new UserModel("", userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), new Date(), new Date(), new ArrayList<>());
        return userDAO.addUser(user);
    }

    public boolean userExists(String email) {
        return userDAO.getUserByEmail(email) != null; // TODO : Replace with a userDAO.exists(email);
    }

}
