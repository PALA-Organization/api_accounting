package fr.pala.accounting.controller;

import fr.pala.accounting.dao.TransactionDAO;
import fr.pala.accounting.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Component
public class TransactionController {

    @Autowired
    TransactionDAO transactionDAO;

    @RequestMapping("/addTransaction")
    public String addTransaction(String account_id, Double amount) {
        TransactionModel transaction = new TransactionModel("1", "achat", "McDo", "Clichy", new Date("16/05/2020"), amount, "test");
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.addTransaction("1", account_id, transaction);
        return "OK";
    }

    // filterAmount & filterDate
}
