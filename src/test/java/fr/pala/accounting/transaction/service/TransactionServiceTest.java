package fr.pala.accounting.transaction.service;

import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.infrastructure.dao.AccountModel;
import fr.pala.accounting.account.service.AccountService;
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

@SpringBootTest(classes = { AccountService.class, TransactionService.class, TransactionDAO.class, AccountDAO.class, UserDAO.class })
// Note : This annotation loads the classes and creates a "fake" context.
// We COULD use the original ServerApplication context but there is a mongo error as we disabled mongo
public class TransactionServiceTest {

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void addTransactionTest() {

        //parameters of addTransaction
        String email = "test@test.fr";
        String account_id = "3234234";

        TransactionModel transaction = new TransactionModel()
                .setId("223435345345")
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

        assertThat(transactionService.createTransaction(email, account_id, transaction.getType(), transaction.getShop_name(),
                transaction.getShop_address(), transaction.getAmount(), transaction.getDescription()).getId()).isEqualTo("223435345345");
    }
}
