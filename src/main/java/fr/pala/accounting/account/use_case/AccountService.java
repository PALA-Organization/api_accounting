package fr.pala.accounting.account.use_case;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;
import fr.pala.accounting.account.infrastructure.controller.AccountNotCreatedException;
import fr.pala.accounting.account.infrastructure.controller.AccountNotFetchedException;
import fr.pala.accounting.account.infrastructure.dao.AccountAdapter;
import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.infrastructure.dao.AccountModel;
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
            System.out.println(newAccount);
            AccountModel newAccountModel = AccountAdapter.accountToModel(newAccount);
            System.out.println(newAccountModel);
            newAccountModel = accountDAO.addAccount(email, newAccountModel);
            System.out.println(newAccountModel);
            return AccountAdapter.modelToAccount(newAccountModel);
        } catch (InvalidFieldException e) {
            throw new AccountNotCreatedException();
        }
    }

    public List<Account> getAccounts(String email) {
        List<AccountModel> accountModels =  accountDAO.getAllAccountsOfUsersByEmail(email);
        try {
            return AccountAdapter.modelListToAccountList(accountModels);
        } catch (InvalidFieldException e) {
            throw new AccountNotCreatedException();
        }
    }

    public void deleteAccount(String email, String accountId) {
        accountDAO.deleteAccount(email, accountId);
    }

    public Account getAccount(String email, String accountId) {
        try {
            return AccountAdapter.modelToAccount(accountDAO.getAccountOfUser(email, accountId));
        } catch (InvalidFieldException e) {
            throw new AccountNotFetchedException();
        }
    }

    public Double getAccountAmount(String email, String accountId) {
        try {
            Account account = AccountAdapter.modelToAccount(accountDAO.getAccountOfUser(email, accountId));
            return account.getAmount();
        } catch (InvalidFieldException e) {
            throw new AccountNotFetchedException();
        }
    }
}
