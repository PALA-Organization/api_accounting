package fr.pala.accounting.account.infrastructure.dao;

import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.user.infrastructure.dao.UserDAO;
import fr.pala.accounting.user.domain.model.UserModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { TransactionDAO.class, AccountDAO.class, UserDAO.class })
public class AccountDAOTest {

    @MockBean
    private MongoTemplate mongoTemplate;
    @Autowired
    private AccountDAO accountDAO;

    @Test
    public void addAccountTest(){
        // given
        String email = "test@test.fr";
        AccountModel account = new AccountModel(null, 23.30, new ArrayList<>());

        // when
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("234", "Test", "test@test.fr", "test", new Date(), new Date(), new ArrayList<AccountModel>()));

        AccountModel accountResult = new AccountModel("34234234", 23.30, new ArrayList<>());
        Mockito.when(mongoTemplate.save(Mockito.any(AccountModel.class))).thenReturn(accountResult);

        // then
        assertThat(accountDAO.addAccount(email, account).getAmount()).isEqualTo(23.30);
    }

    @Test
    public void getAllAccountsOfUsersByIdTest() {
        // given
        String user_id = "12";

        AccountModel account = new AccountModel("", 234.55, new ArrayList<String>());
        AccountModel account2 = new AccountModel("", 234.55,  new ArrayList<String>());

        // when
        ArrayList<AccountModel> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user_id));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test", "test@test.fr", new Date(), new Date(), accounts));

        // then
        assertThat(accountDAO.getAllAccountsOfUsersByUser_id(user_id)).hasSize(2);
    }


    @Test
    public void getAllAccountsOfUsersByEmailTest() {
        // given
        String email = "test@test.fr";

        AccountModel account = new AccountModel("", 234.55, new ArrayList<String>());
        AccountModel account2 = new AccountModel("", 234.55,  new ArrayList<String>());

        // when
        ArrayList<AccountModel> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test", "test@test.fr", new Date(), new Date(), accounts));

        // then
        assertThat(accountDAO.getAllAccountsOfUserByEmail(email)).hasSize(2);
    }

    @Test
    public void getAccountOfUserTest() {
        // given
        String email = "test@test.fr";
        String account_id = "13";

        // when
        AccountModel account = new AccountModel(account_id, 234.55, new ArrayList<String>());
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(account_id));
        Mockito.when(mongoTemplate.findOne(query, AccountModel.class))
                .then(ignoredInvocation -> account);

        ArrayList<AccountModel> accounts = new ArrayList<>();
        accounts.add(account);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query1, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test@test.fr", "test", new Date(), new Date(), accounts));

        // then
        assertThat(accountDAO.getAccountOfUser(email, account_id).getId()).isEqualTo(account_id);
    }

    /*
    @Test
    public void deleteAccountOfUserTest() {
        // given
        String user_id = new ObjectId().toString();
        String email = "test@test.fr";
        String accountId = new ObjectId().toString();
        String account2Id = new ObjectId().toString();

        AccountModel account = new AccountModel(accountId, 23.30, new ArrayList<>());
        AccountModel accountToDelete = new AccountModel(account2Id, 234.55,  new ArrayList<String>());

        // when
        ArrayList<AccountModel> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(accountToDelete);
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel(user_id, "Test", "test@test.fr", "test", new Date(), new Date(), accounts));

        Query queryUser = new Query();
        queryUser.addCriteria(Criteria.where("_id").is(user_id));
        ArrayList<AccountModel> accountsModified = new ArrayList<>();
        accountsModified.add(account);
        UserModel userResult = new UserModel(user_id, "Test", "test@test.fr", "test", new Date(), new Date(), accountsModified);

        Update update = new Update();
        update.set("accounts", accounts);
        Mockito.when(mongoTemplate.findAndModify(queryUser, update, UserModel.class)).thenReturn(userResult).then(accountsModified);
        // then
        assertThat(accountDAO.getAllAccountsOfUserByEmail(email)).isEqualTo(accountsModified);
    }
    */
}
