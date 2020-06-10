package fr.pala.accounting.controller;

import fr.pala.accounting.dao.AccountDAO;
import fr.pala.accounting.dao.TransactionDAO;
import fr.pala.accounting.dao.UserDAO;
import fr.pala.accounting.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

public class TransactionController {

    @Autowired
    TransactionDAO transactionDAO;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    UserDAO userDAO;

    @RequestMapping("/addTransaction")
    public String addTransaction(String user_id, String account_id, Double amount) {

        TransactionModel transaction = new TransactionModel("1", "Restaurant", "McDo", "Clichy", new Date(), amount, "test");
        // TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.addTransaction(user_id, account_id, transaction);
        return "OK";
    }

    @RequestMapping("/updateTransaction")
    public String updateTransaction(String user_id, String account_id, Double amount) {

        // TransactionModel transaction = new TransactionModel("1", "Restaurant", "McDo", "Clichy", new Date(), amount, "test");
        // TransactionDAO transactionDAO = new TransactionDAO();
        // transactionDAO.addTransaction(user_id, account_id, transaction);
        return "OK";
    }

    @RequestMapping("/filter")
    public String filter(String user_id, String type) {
        // filtrer par montant get dans transaction

        // id_user dans le token, parser tous les comptes, toutes les transactions sup à tant ou inf à tant
        // transaction/id_account qui va aller
        return "OK";
    }

}
