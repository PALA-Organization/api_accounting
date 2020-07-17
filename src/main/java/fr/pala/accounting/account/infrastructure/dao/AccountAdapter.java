package fr.pala.accounting.account.infrastructure.dao;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.domain.model.InvalidFieldException;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter {
    public static AccountMongoModel accountToModel(Account account) {
        return new AccountMongoModel(account.getId(), account.getAmount(), account.getTransactions_ids());
    }

    public static Account modelToAccount(AccountMongoModel accountMongoModel) throws InvalidFieldException {
        return new Account(accountMongoModel.getId(), accountMongoModel.getAmount(), accountMongoModel.getTransactions_ids());
    }

    public static List<Account> modelListToAccountList(List<AccountMongoModel> accountMongoModels) throws InvalidFieldException {
        List<Account> accounts = new ArrayList<>();
        for (AccountMongoModel accountMongoModel : accountMongoModels) {
            accounts.add(modelToAccount(accountMongoModel));
        }
        return accounts;
    }

    public static List<AccountMongoModel> transactionListToModelList(List<Account> accounts) {
        List<AccountMongoModel> accountMongoModels = new ArrayList<>();
        for (Account account : accounts) {
            accountMongoModels.add(accountToModel(account));
        }
        return accountMongoModels;
    }
}
