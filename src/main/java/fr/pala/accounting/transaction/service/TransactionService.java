package fr.pala.accounting.transaction.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.AccountService;
import fr.pala.accounting.account.service.exception.AccountNotUpdatedException;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
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

    public Transaction createTransaction(String email, String accountId, String type, String shop_name, String shop_address, Double amount, String description) {
        UserModel user = userDAO.getUserByEmail(email);
        if(user == null) {
            throw new UserNotFoundException();
        }
        Transaction transaction = new Transaction(null, type, shop_name, shop_address, new Date(), amount, description);
        Transaction transactionResult;
        try {
            transactionResult = transactionDAO.addTransaction(transaction);
            Account account = accountService.getAccount(email, accountId);
            List<String> transactions_ids = account.getTransactions_ids();
            transactions_ids.add(transactionResult.getId());
            account.setTransactions_ids(transactions_ids);
            accountService.updateAccount(email, account);
        } catch (AccountNotUpdatedException | InvalidFieldException e) {
            throw new TransactionNotCreatedException(e);
        }

        return transactionResult;
    }

    public Transaction registerScanTransaction(String email, String accountId, String shop_name, String shop_address, Double amount, String description) {
        return createTransaction(email, accountId, "Ticket", shop_name, shop_address, amount, description);
    }
}
