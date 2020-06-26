package fr.pala.accounting.transaction.dao;

import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.infrastructure.dao.AccountModel;
import fr.pala.accounting.account.service.AccountService;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionAdapter;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionModel;
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

@SpringBootTest(classes = { AccountService.class, TransactionDAO.class, AccountDAO.class, UserDAO.class })
// Note : This annotation loads the classes and creates a "fake" context.
// We COULD use the original ServerApplication context but there is a mongo error as we disabled mongo
public class TransactionDAOTest {

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private TransactionDAO transactionDAO;

    @Test
    public void getAllTransactionsOfNoAccountTest() throws InvalidFieldException {
        //Parameters of getAllTransactionsOfAccount
        String email = "test@test.fr";
        String account_id = "12";

        //Mock the user get
        ArrayList<AccountModel> accounts = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test@test.fr", "test", new Date(), new Date(), accounts));
        //Mockito.when(accountDAO.getAccountOfUser(email, account_id)).then(ignoredInvocation -> new )

        assertThat(transactionDAO.getAllTransactionsOfAccount(email, account_id)).hasSize(0);

    }

    @Test
    public void getAllTransactionsOfAccountTest() throws InvalidFieldException {
        //Parameters of getAllTransactionsOfAccount
        String email = "test@test.fr";
        String account_id = "12";

        TransactionModel transactionModel = new TransactionModel("235","Test", "Test", "Test", null, 10D, "Test");
        TransactionModel transactionModel2 = new TransactionModel("235","Test", "Test", "Test", null, 10D, "Test");
        //Mock the user get
        ArrayList<AccountModel> accounts = new ArrayList<>();
        ArrayList<String> transactionsIds = new ArrayList<String>();
        transactionsIds.add(transactionModel.getId());
        transactionsIds.add(transactionModel2.getId());
        AccountModel account = new AccountModel(account_id, 234.55, transactionsIds);
        accounts.add(account);

        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test@test.fr", "test", new Date(), new Date(), accounts));

        Query queryTransaction = new Query();
        queryTransaction.addCriteria(Criteria.where("id").is(transactionModel.getId()));
        Mockito.when(mongoTemplate.findOne(queryTransaction, TransactionModel.class)).thenReturn(transactionModel);
        Query queryTransaction2 = new Query();
        queryTransaction2.addCriteria(Criteria.where("id").is(transactionModel2.getId()));
        Mockito.when(mongoTemplate.findOne(queryTransaction, TransactionModel.class)).thenReturn(transactionModel2);

        assertThat(transactionDAO.getAllTransactionsOfAccount(email, account_id)).hasSize(2);
    }

    @Test
    public void getTransactionTest() throws InvalidFieldException {
        // given
        String transactionId = "223435345345";

        TransactionModel transactionModel = new TransactionModel()
                .setId("223435345345")
                .setType("Test")
                .setShop_name("Auchan")
                .setShop_address("Test")
                .setDate(new Date())
                .setAmount(33.70)
                .setDescription("Test");

        // when
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(transactionId));
        Mockito.when(mongoTemplate.findOne(query, TransactionModel.class))
                .thenReturn(transactionModel);

        // then
        Transaction transaction = TransactionAdapter.modelToTransaction(transactionModel);
        assertThat(transactionDAO.getTransaction(transactionId).getId()).isEqualTo(transaction.getId());
    }
}
