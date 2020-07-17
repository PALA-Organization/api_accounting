package fr.pala.accounting.account.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;
import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.service.exception.AccountNotCreatedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CreateAccount {
    private final AccountDAO accountDAO;

    public CreateAccount(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account createAccount(String email) {
        Account newAccount;
        try {
            newAccount = new Account(null, (double) 0, new ArrayList<>());
            return accountDAO.addAccount(email, newAccount);
        } catch (InvalidFieldException e) {
            throw new AccountNotCreatedException();
        }
    }
}
