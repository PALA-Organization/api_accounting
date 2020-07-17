package fr.pala.accounting.account.service;

import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import org.springframework.stereotype.Service;

@Service
public class DeleteAccount {
    private final AccountDAO accountDAO;

    public DeleteAccount(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void deleteAccount(String email, String accountId) {
        accountDAO.deleteAccount(email, accountId);
    }
}
