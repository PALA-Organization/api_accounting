package fr.pala.accounting.account.infrastructure.dao;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter {
    public static AccountModel accountToModel(Account account) {
        return new AccountModel(account.getId(), account.getAmount(), account.getTransactions_ids());
    }

    public static Account modelToAccount(AccountModel accountModel) throws InvalidFieldException {
        return new Account(accountModel.getId(), accountModel.getAmount(), accountModel.getTransactions_ids());
    }

    public static List<Account> modelListToAccountList(List<AccountModel> accountModels) throws InvalidFieldException {
        List<Account> accounts = new ArrayList<>();
        for (AccountModel accountModel : accountModels) {
            accounts.add(modelToAccount(accountModel));
        }
        return accounts;
    }

    public static List<AccountModel> transactionListToModelList(List<Account> accounts) {
        List<AccountModel> accountModels = new ArrayList<>();
        for (Account account : accounts) {
            accountModels.add(accountToModel(account));
        }
        return accountModels;
    }
}
