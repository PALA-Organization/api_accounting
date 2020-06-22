package fr.pala.accounting.dao;

import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.infrastructure.dao.AccountModel;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.transaction.domain.model.TransactionModel;
import fr.pala.accounting.user.domain.model.UserModel;
import fr.pala.accounting.user.infrastructure.dao.UserDAO;
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
// Note : This annotation loads the classes and creates a "fake" context.
// We COULD use the original ServerApplication context but there is a mongo error as we disabled mongo
public class TransactionDAOTest {

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private TransactionDAO transactionDAO;

    @Test
    public void getAllTransactionsOfNoAccountTest() {
        //Parameters of getAllTransactionsOfAccount
        String email = "test@test.fr";
        String account_id = "12";

        //Mock the user get
        ArrayList<AccountModel> accounts = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test@test.fr", "test", new Date(), new Date(), accounts));

        assertThat(transactionDAO.getAllTransactionsOfAccount(email, account_id)).hasSize(0);
    }

    @Test
    public void getAllTransactionsOfAccountTest() {
        //Parameters of getAllTransactionsOfAccount
        String email = "test@test.fr";
        String account_id = "12";

        //Mock the user get
        ArrayList<AccountModel> accounts = new ArrayList<>();
        ArrayList<String> transactionsIds = new ArrayList<String>();
        transactionsIds.add("235");
        transactionsIds.add("444");
        AccountModel account = new AccountModel("12", 234.55, transactionsIds);
        accounts.add(account);

        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test@test.fr", "test", new Date(), new Date(), accounts));

        assertThat(transactionDAO.getAllTransactionsOfAccount(email, account_id)).hasSize(2);
    }

    @Test
    public void getTransactionTest(){
        //parameters of getTransaction
        String transactionId = "223435345345";

        TransactionModel transaction = new TransactionModel()
                .setTransaction_id("223435345345")
                .setType("Test")
                .setShop_name("Auchan")
                .setShop_address("Test")
                .setDate(new Date())
                .setAmount(33.70)
                .setDescription("Test");

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(transactionId));
        Mockito.when(mongoTemplate.findOne(query, TransactionModel.class))
                .then(ignoredInvocation -> transaction);

        assertThat(transactionDAO.getTransaction(transactionId)).isEqualTo(transaction);
    }


    @Test
    public void addTransactionTest() {

        //parameters of addTransaction
        String email = "test@test.fr";
        String account_id = "3234234";

        TransactionModel transaction = new TransactionModel()
                .setTransaction_id("223435345345")
                .setType("Test")
                .setShop_name("Auchan")
                .setShop_address("Test")
                .setDate(new Date())
                .setAmount(33.70)
                .setDescription("Test");

        //Mock the account get
        ArrayList<String> transactionsIds = new ArrayList<String>();
        transactionsIds.add("235");
        transactionsIds.add("444");
        AccountModel account = new AccountModel(account_id, 234.55, transactionsIds);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("account_id").is(account_id));
        Mockito.when(mongoTemplate.findOne(query1, AccountModel.class))
                .then(ignoredInvocation -> account);

        //mock the user get
        ArrayList<AccountModel> accounts = new ArrayList<>();
        accounts.add(account);
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test@test.fr", "test", new Date(), new Date(), accounts));

        Mockito.when(mongoTemplate.save(Mockito.any(TransactionModel.class))).thenReturn(transaction);

        assertThat(transactionDAO.addTransaction(email, account_id, transaction).getId()).isEqualTo("223435345345");
    }
}
