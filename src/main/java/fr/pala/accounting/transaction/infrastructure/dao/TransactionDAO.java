package fr.pala.accounting.transaction.infrastructure.dao;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.*;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.service.exception.TransactionNotFoundException;
import fr.pala.accounting.transaction.service.exception.TransactionNotUpdatedException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionDAO {
    private final GetAccount getAccount;
    private final MongoTemplate mongoTemplate;


    public TransactionDAO(GetAccount getAccount, MongoTemplate mongoTemplate) {
        this.getAccount = getAccount;
        this.mongoTemplate = mongoTemplate;
    }


    public List<Transaction> getAllTransactionsOfAccount(String user_id, String account_id) throws InvalidFieldException, TransactionNotFoundException {
        List<Transaction> transactionResults = new ArrayList<>();
        Account account = getAccount.getAccount(user_id, account_id);

        if (account == null) {
            return transactionResults;
        }

        List<String> transactions_ids = account.getTransactions_ids();

        for (String transaction_id : transactions_ids) {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(transaction_id));
            TransactionMongoModel transactionMongoModel = mongoTemplate.findOne(query, TransactionMongoModel.class);
            if (transactionMongoModel != null) {
                Transaction transaction = TransactionAdapter.modelToTransaction(transactionMongoModel);
                transactionResults.add(transaction);
            }
        }

        return transactionResults;
    }

    public Transaction getTransaction(String transaction_id) throws InvalidFieldException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(transaction_id));
        TransactionMongoModel transactionMongoModel = mongoTemplate.findOne(query, TransactionMongoModel.class);
        if (transactionMongoModel != null) {
            return TransactionAdapter.modelToTransaction(transactionMongoModel);
        }
        throw new TransactionNotFoundException();
    }

    public Transaction addTransaction(Transaction transaction) throws InvalidFieldException {
        final TransactionMongoModel transactionMongoModel = TransactionAdapter.transactionToModel(transaction);
        final TransactionMongoModel savedTransaction = mongoTemplate.save(transactionMongoModel);
        return TransactionAdapter.modelToTransaction(savedTransaction);
    }

    public Transaction updateTransaction(Transaction transaction) throws InvalidFieldException {
        TransactionMongoModel transactionMongoModel = TransactionAdapter.transactionToModel(transaction);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(transactionMongoModel.getId()));
        Update update = new Update();
        update.set("type", transactionMongoModel.getType());
        update.set("shop_name", transactionMongoModel.getShop_name());
        update.set("shop_address", transactionMongoModel.getShop_address());
        update.set("date", transactionMongoModel.getDate());
        update.set("amount", transactionMongoModel.getAmount());
        update.set("description", transactionMongoModel.getDescription());

        TransactionMongoModel updatedTransactionMongoModel = mongoTemplate.findAndModify(query, update, TransactionMongoModel.class);
        if (updatedTransactionMongoModel != null) {
            return TransactionAdapter.modelToTransaction(updatedTransactionMongoModel);
        }
        throw new TransactionNotUpdatedException("Transaction neither found nor modified");
    }

    public void deleteTransaction(Transaction transaction) {
        TransactionMongoModel transactionMongoModel = TransactionAdapter.transactionToModel(transaction);
        mongoTemplate.remove(transactionMongoModel);
    }

}
