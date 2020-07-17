package fr.pala.accounting.account.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;
import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.service.exception.AccountNotFetchedException;
import fr.pala.accounting.account.service.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetAccountAmount {
    private final AccountDAO accountDAO;

    public GetAccountAmount(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Double getAccountAmount(String email, String accountId) {
        try {
            Account account = accountDAO.getAccountOfUser(email, accountId);
            if (account == null)
                throw new AccountNotFoundException();
            return account.getAmount();
        } catch (InvalidFieldException e) {
            throw new AccountNotFetchedException();
        }
    }
}
