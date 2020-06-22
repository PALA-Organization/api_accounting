package fr.pala.accounting.transaction.infrastructure.dao;

import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter {
    public static TransactionModel transactionToModel(Transaction transaction) {
        return new TransactionModel(transaction.getId(), transaction.getType(), transaction.getShop_name(), transaction.getShop_address(), transaction.getDate(), transaction.getAmount(), transaction.getDescription());
    }

    public static Transaction modelToTransaction(TransactionModel transactionModel) throws InvalidFieldException {
        return new Transaction(transactionModel.getId(), transactionModel.getType(), transactionModel.getShop_name(), transactionModel.getShop_address(), transactionModel.getDate(), transactionModel.getAmount(), transactionModel.getDescription());
    }

    public static List<Transaction> modelListToAccountList(List<TransactionModel> transactionModels) throws InvalidFieldException {
        List<Transaction> transactions = new ArrayList<>();
        for (TransactionModel transactionModel : transactionModels) {
            transactions.add(modelToTransaction(transactionModel));
        }
        return transactions;
    }
}
