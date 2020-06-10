package fr.pala.accounting.controller;

import fr.pala.accounting.model.AccountModel;
import fr.pala.accounting.user.dao.UserDAO;
import fr.pala.accounting.user.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@RestController("/")
public class HomeController implements ErrorController {

    final
    UserDAO userDAO;

    private static final String PATH = "/error";

    public HomeController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping
    public String index() {
        return "home";
    }

    @RequestMapping(value = PATH)
    public String error() {
        return "Error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping("/addUsers")
    public String addUsers() {
        ArrayList<AccountModel> accounts = new ArrayList<>();

        AccountModel account = new AccountModel("", 23.3, null);
        accounts.add(account);

        UserModel user = new UserModel(null, "Test", "test@test.fr", "Test", new Date(),
                new Date(), accounts);

        userDAO.addUser(user);
        return "Test user created";
    }
}
