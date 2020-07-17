package fr.pala.accounting.account.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;
import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.service.exception.AccountNotCreatedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllAccounts {
    private final AccountDAO accountDAO;

    public GetAllAccounts(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts(String email) {
        try {
            return accountDAO.getAllAccountsOfUserByEmail(email);
        } catch (InvalidFieldException e) {
            throw new AccountNotCreatedException();
        }
    }
}
