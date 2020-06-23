package fr.pala.accounting.account.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;
import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.service.exception.AccountDoesNotExistException;
import fr.pala.accounting.account.service.exception.AccountNotCreatedException;
import fr.pala.accounting.account.service.exception.AccountNotFetchedException;
import fr.pala.accounting.account.service.exception.AccountNotUpdatedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
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


    public Account updateAccount(String email, Account account) {

        try {
            return accountDAO.updateAccount(email, account);
        } catch (InvalidFieldException e) {
            throw new AccountNotUpdatedException(e);
        }
    }

    public List<Account> getAllAccounts(String email) {
        try {
            return accountDAO.getAllAccountsOfUserByEmail(email);
        } catch (InvalidFieldException e) {
            throw new AccountNotCreatedException();
        }
    }

    public void deleteAccount(String email, String accountId) {
        accountDAO.deleteAccount(email, accountId);
    }

    public Account getAccount(String email, String account_id) {
        try {
            Account account = accountDAO.getAccountOfUser(email, account_id);
            if (account == null)
                throw new AccountDoesNotExistException();
            return account;
        } catch (InvalidFieldException e) {
            throw new AccountNotFetchedException();
        }
    }

    public Double getAccountAmount(String email, String accountId) {
        try {
            Account account = accountDAO.getAccountOfUser(email, accountId);
            return account.getAmount();
        } catch (InvalidFieldException e) {
            throw new AccountNotFetchedException();
        }
    }
}
