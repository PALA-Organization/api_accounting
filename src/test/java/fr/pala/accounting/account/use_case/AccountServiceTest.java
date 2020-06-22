package fr.pala.accounting.account.use_case;

import fr.pala.accounting.account.infrastructure.dao.AccountDAO;
import fr.pala.accounting.account.infrastructure.dao.AccountModel;
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

@SpringBootTest(classes = { AccountService.class, AccountDAO.class, UserDAO.class })
public class AccountServiceTest {

    @MockBean
    private MongoTemplate mongoTemplate;
    @Autowired
    private AccountService accountService;

    @Test
    public void getAmountOfAccountTest() {
        //Parameters of getAmountOfAccount
        String email = "test@test.frZ";
        String account_id = "13";

        //Mock an account get
        AccountModel account = new AccountModel(account_id, 234.55, new ArrayList<String>());
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("_id").is(account_id));
        Mockito.when(mongoTemplate.findOne(query1, AccountModel.class))
                .then(ignoredInvocation -> account);

        //mock the user get
        ArrayList<AccountModel> accounts = new ArrayList<>();
        accounts.add(account);
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Mockito.when(mongoTemplate.findOne(query, UserModel.class))
                .then(ignoredInvocation -> new UserModel("32352453234", "Test", "test@test?fr", "test", new Date(), new Date(), accounts));

        assertThat(accountService.getAccountAmount(email, account_id)).isEqualTo(234.55);
    }
}
