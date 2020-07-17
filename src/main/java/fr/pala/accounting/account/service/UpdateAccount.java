package fr.pala.accounting.account.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;
import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.service.exception.AccountNotUpdatedException;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccount {
    private final AccountDAO accountDAO;

    public UpdateAccount(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account updateAccount(String email, Account account) {
        try {
            return accountDAO.updateAccount(email, account);
        } catch (InvalidFieldException e) {
            throw new AccountNotUpdatedException(e);
        }
    }
}
