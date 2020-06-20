package fr.pala.accounting.transaction;

import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.infrastructure.dao.AccountModel;
import fr.pala.accounting.transaction.model.TransactionModel;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionDAO {
    private final AccountDAO accountDAO;
    private final MongoTemplate mongoTemplate;


    public TransactionDAO(AccountDAO accountDAO, MongoTemplate mongoTemplate) {
        this.accountDAO = accountDAO;
        this.mongoTemplate = mongoTemplate;
    }


    public List<TransactionModel> getAllTransactionsOfAccount(String user_id, String account_id) {
        List<TransactionModel> transactionResults = new ArrayList<TransactionModel>();
        AccountModel accountModel = accountDAO.getAccountOfUser(user_id, account_id);

        if(accountModel == null){
            return transactionResults;
        }

        List<String> transactions_ids = accountModel.getTransactions_ids();

        for (String transactions_id : transactions_ids) {
            Query query = new Query();
            query.addCriteria(Criteria.where("transaction_id").is(transactions_id));
            transactionResults.add(mongoTemplate.findOne(query, TransactionModel.class));
        }

        return transactionResults;
    }

    public TransactionModel getTransaction(String transaction_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("transaction_id").is(transaction_id));
        return mongoTemplate.findOne(query, TransactionModel.class);
    }

    public TransactionModel addTransaction(String user_id, String account_id, TransactionModel transactionModel) {
        TransactionModel transactionResult = mongoTemplate.save(transactionModel);

        //add transaction to account
        AccountModel account = accountDAO.getAccountOfUser(user_id, account_id);
        List<String> transactions_ids = account.getTransactions_ids();
        transactions_ids.add(transactionResult.getTransaction_id());
        account.setTransactions_ids(transactions_ids);
        accountDAO.updateAccount(user_id, account_id, account);
        return transactionResult;
    }

    public void updateTransaction(TransactionModel transactionModel) {
        Query query = new Query();
        query.addCriteria(Criteria.where("transaction_id").is(transactionModel.getTransaction_id()));
        Update update = new Update();
        update.set("type", transactionModel.getType());
        update.set("shop_name", transactionModel.getShop_name());
        update.set("shop_address", transactionModel.getShop_address());
        update.set("date", transactionModel.getDate());
        update.set("amount", transactionModel.getAmount());
        update.set("description", transactionModel.getDescription());

        mongoTemplate.findAndModify(query, update, TransactionModel.class);
    }

    public void deleteTransaction(String user_id, String account_id, TransactionModel transaction) {

        mongoTemplate.remove(transaction);

        AccountModel account = accountDAO.getAccountOfUser(user_id, account_id);
        List<String> transactions_ids = account.getTransactions_ids();

        for (int i = 0; i < transactions_ids.size(); i++) {
            if(transactions_ids.get(i).equals(transaction.getTransaction_id())){
                transactions_ids.remove(i);
                break;
            }
        }
        account.setTransactions_ids(transactions_ids);
        accountDAO.updateAccount(user_id, account_id, account);
    }

}
