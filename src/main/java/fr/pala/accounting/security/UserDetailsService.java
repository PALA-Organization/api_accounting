package fr.pala.accounting.security;

import fr.pala.accounting.user.UserDAO;
import fr.pala.accounting.user.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserDAO userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO appUser = userService.getUserByEmail(email).toDTO();

        if(appUser == null){
            throw new AuthenticationServiceException("email " + email + " not found");
        }

        return User.builder()
                .username(email)
                .password(appUser.getPassword())
                .roles("")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}