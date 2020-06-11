package fr.pala.accounting.account.model;

import fr.pala.accounting.user.UserDAO;
import fr.pala.accounting.user.model.UserModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDAO {

    private final MongoTemplate mongoTemplate;

    private final UserDAO userDAO;

    public AccountDAO(MongoTemplate mongoTemplate, UserDAO userDAO) {
        this.mongoTemplate = mongoTemplate;
        this.userDAO = userDAO;
    }

    public AccountModel addAccount(String user_id, AccountModel accountModel) {

        //to set an autogenerated Id to the account
        accountModel.setAccount_id(new ObjectId().toString());

        UserModel user = userDAO.getUserById(user_id);
        List<AccountModel> accounts = (List<AccountModel>) user.getAccounts();
        accounts.add(accountModel);

        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(user.getUser_id()));
        Update update = new Update();
        update.set("accounts", accounts);
        mongoTemplate.findAndModify(query, update, UserModel.class);

        return accountModel;
    }

    public List<AccountModel> getAllAccountsOfUsers(String user_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(user_id));

        UserModel user = mongoTemplate.findOne(query, UserModel.class);

        if(user != null){
            return (List<AccountModel>) user.getAccounts();
        }
        return null;
    }

    public AccountModel getAccountOfUser(String user_id, String account_id) {
        List<AccountModel> accounts = getAllAccountsOfUsers(user_id);

        AccountModel accountResult = null;
        for (AccountModel account : accounts) {
            if (account.getAccount_id().equals(account_id)) {
                accountResult = account;
                break;
            }
        }
        return accountResult;
    }

    public Double getAmountOfAccount(String user_id, String account_id) {
        AccountModel account = getAccountOfUser(user_id, account_id);
        return account.getAmount();
    }

    public void updateAccount(String user_id, String account_id, AccountModel account) {
        UserModel user = userDAO.getUserById(user_id);
        List<AccountModel> accounts = (List<AccountModel>) user.getAccounts();

        for (AccountModel accountModel : accounts) {
            if (accountModel.getAccount_id().equals(account_id)) {
                accountModel.setAmount(account.getAmount());
                accountModel.setTransactions_ids(account.getTransactions_ids());
                break;
            }
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(user.getUser_id()));
        Update update = new Update();
        update.set("accounts", accounts);
        mongoTemplate.findAndModify(query, update, UserModel.class);
    }

    public void deleteAccount(String user_id, String account_id) {

        UserModel user = userDAO.getUserById(user_id);
        List<AccountModel> accounts = (List<AccountModel>) user.getAccounts();

        for (int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).getAccount_id().equals(account_id)){
                accounts.remove(i);
                break;
            }
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(user.getUser_id()));
        Update update = new Update();
        update.set("accounts", accounts);
        mongoTemplate.findAndModify(query, update, UserModel.class);
    }
}
