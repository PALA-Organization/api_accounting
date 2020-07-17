package fr.pala.accounting.account.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;
import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.service.exception.AccountNotFetchedException;
import org.springframework.stereotype.Service;

@Service
public class GetAccount {
    private final AccountDAO accountDAO;

    public GetAccount(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account getAccount(String email, String account_id) {
        try {
            return accountDAO.getAccountOfUser(email, account_id);
        } catch (InvalidFieldException e) {
            throw new AccountNotFetchedException(e);
        }
    }
}
