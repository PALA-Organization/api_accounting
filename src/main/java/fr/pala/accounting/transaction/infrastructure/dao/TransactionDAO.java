package fr.pala.accounting.transaction.infrastructure.dao;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.AccountService;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
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
    private final AccountService accountService;
    private final MongoTemplate mongoTemplate;


    public TransactionDAO(AccountService accountService, MongoTemplate mongoTemplate) {
        this.accountService = accountService;
        this.mongoTemplate = mongoTemplate;
    }


    public List<Transaction> getAllTransactionsOfAccount(String user_id, String account_id) throws InvalidFieldException {
        List<Transaction> transactionResults = TransactionAdapter.modelListToTransactionList(new ArrayList<TransactionModel>());
        Account account = accountService.getAccount(user_id, account_id);

        if (account == null) {
            return transactionResults;
        }

        List<String> transactions_ids = account.getTransactions_ids();

        for (String transactions_id : transactions_ids) {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(transactions_id));
            TransactionModel transactionModel = mongoTemplate.findOne(query, TransactionModel.class);
            if (transactionModel != null) {
                Transaction transaction = TransactionAdapter.modelToTransaction(transactionModel);
                transactionResults.add(transaction);
            }

        }

        return transactionResults;
    }

    public Transaction getTransaction(String transaction_id) throws InvalidFieldException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(transaction_id));
        TransactionModel transactionModel = mongoTemplate.findOne(query, TransactionModel.class);
        if (transactionModel != null) {
            return TransactionAdapter.modelToTransaction(transactionModel);
        }
        return null;
    }

    public Transaction addTransaction(Transaction transaction) throws InvalidFieldException {
        final TransactionModel transactionModel = TransactionAdapter.transactionToModel(transaction);
        final TransactionModel savedTransaction = mongoTemplate.save(transactionModel);
        return TransactionAdapter.modelToTransaction(savedTransaction);
    }

    public Transaction updateTransaction(Transaction transaction) throws InvalidFieldException {
        TransactionModel transactionModel = TransactionAdapter.transactionToModel(transaction);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(transactionModel.getId()));
        Update update = new Update();
        update.set("type", transactionModel.getType());
        update.set("shop_name", transactionModel.getShop_name());
        update.set("shop_address", transactionModel.getShop_address());
        update.set("date", transactionModel.getDate());
        update.set("amount", transactionModel.getAmount());
        update.set("description", transactionModel.getDescription());

        TransactionModel updatedTransactionModel = mongoTemplate.findAndModify(query, update, TransactionModel.class);
        if (updatedTransactionModel != null) {
            return TransactionAdapter.modelToTransaction(updatedTransactionModel);
        }
        throw new TransactionNotUpdatedException();
    }

    public void deleteTransaction(String email, String account_id, TransactionModel transaction) {

        mongoTemplate.remove(transaction);

        Account account = accountService.getAccount(email, account_id);
        List<String> transactions_ids = account.getTransactions_ids();

        for (int i = 0; i < transactions_ids.size(); i++) {
            if(transactions_ids.get(i).equals(transaction.getId())){
                transactions_ids.remove(i);
                break;
            }
        }
        account.setTransactions_ids(transactions_ids);
        accountService.updateAccount(email, account);
    }

}
