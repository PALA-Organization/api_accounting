package fr.pala.accounting.transaction.infrastructure.dao;

import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter {
    public static TransactionMongoModel transactionToModel(Transaction transaction) {
        return new TransactionMongoModel(transaction.getId(), transaction.getType(), transaction.getShop_name(), transaction.getShop_address(), transaction.getDate(), transaction.getAmount(), transaction.getDescription());
    }

    public static Transaction modelToTransaction(TransactionMongoModel transactionMongoModel) throws InvalidFieldException {
        return new Transaction(transactionMongoModel.getId(), transactionMongoModel.getType(), transactionMongoModel.getShop_name(), transactionMongoModel.getShop_address(), transactionMongoModel.getDate(), transactionMongoModel.getAmount(), transactionMongoModel.getDescription());
    }

    public static List<Transaction> modelListToTransactionList(List<TransactionMongoModel> transactionMongoModels) throws InvalidFieldException {
        List<Transaction> transactions = new ArrayList<>();
        for (TransactionMongoModel transactionMongoModel : transactionMongoModels) {
            transactions.add(modelToTransaction(transactionMongoModel));
        }
        return transactions;
    }
}
