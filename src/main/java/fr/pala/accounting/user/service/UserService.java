package fr.pala.accounting.user.service;

import fr.pala.accounting.user.infrastructure.dao.UserDAO;
import fr.pala.accounting.user.infrastructure.controller.UserDTO;
import fr.pala.accounting.user.domain.model.UserModel;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserModel createUser(UserDTO userDTO){
        return userDAO.addUser(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());

    }

}
