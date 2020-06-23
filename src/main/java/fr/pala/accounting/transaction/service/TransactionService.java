package fr.pala.accounting.transaction.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.AccountService;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.transaction.infrastructure.controller.TransactionDTO;
import fr.pala.accounting.transaction.domain.model.TransactionModel;
import fr.pala.accounting.user.infrastructure.dao.UserDAO;
import fr.pala.accounting.user.service.exception.UserNotFoundException;
import fr.pala.accounting.user.domain.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final AccountService accountService;
    private final UserDAO userDAO;

    public TransactionService(TransactionDAO transactionDAO, AccountService accountService, UserDAO userDAO) {
        this.transactionDAO = transactionDAO;
        this.accountService = accountService;
        this.userDAO = userDAO;
    }

    public String createTransaction(String email, String accountId, String type, String shop_name, String shop_address, Double amount, String description) {
        UserModel user = userDAO.getUserByEmail(email);
        if(user == null) {
            throw new UserNotFoundException();
        }
        Transaction transaction = new Transaction(null, type, shop_name, shop_address, new Date(), amount, description);

        try {
            transaction = transactionDAO.addTransaction(transaction);
        } catch (InvalidFieldException e) {
            throw new TransactionNotCreatedException();
        }



        //add transaction to account
        List<Account> account = accountService.getAccounts(email);
        List<String> transactions_ids = account.getTransactions_ids();
        transactions_ids.add(transactionResult.getId());
        account.setTransactions_ids(transactions_ids);
        accountDAO.updateAccount(email, account_id, account);

        if (transaction == null) {
            throw new TransactionNotCreatedException();
        }

        return transaction.getTransaction_id();
    }

    public String registerScanTransaction(String userName, String shop_name, String shop_address, Double amount, String description, String accountId) {
        UserModel user = userDAO.getUserByEmail("test");

        TransactionDTO transactionDTO = new TransactionDTO()
                .setType("Ticket")
                .setShop_name(shop_name)
                .setShop_address(shop_address)
                .setAmount(amount)
                .setDescription(description);

        return createTransaction(userName, accountId, transactionDTO);
    }
}
