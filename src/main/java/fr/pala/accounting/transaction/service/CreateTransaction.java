package fr.pala.accounting.transaction.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.GetAccount;
import fr.pala.accounting.account.service.UpdateAccount;
import fr.pala.accounting.account.service.exception.AccountNotUpdatedException;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.transaction.service.exception.TransactionNotCreatedException;
import fr.pala.accounting.user.infrastructure.dao.UserDAO;
import fr.pala.accounting.user.service.CreateUser;
import fr.pala.accounting.user.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CreateTransaction {
    private final TransactionDAO transactionDAO;
    private final CreateUser createUser;
    private final GetAccount getAccount;
    private final UpdateAccount updateAccount;

    public CreateTransaction(TransactionDAO transactionDAO, CreateUser createUser, GetAccount getAccount, UpdateAccount updateAccount, UserDAO userDAO) {
        this.transactionDAO = transactionDAO;
        this.createUser = createUser;
        this.getAccount = getAccount;
        this.updateAccount = updateAccount;
    }

    public Transaction createTransaction(String email, String accountId, String type, String shop_name, String shop_address, Double amount, String description) {
        Transaction transaction = new Transaction(null, type, shop_name, shop_address, new Date(), amount, description);

        try {
            Transaction savedTransaction = transactionDAO.addTransaction(transaction);
            processNewTransactionToAccount(email, accountId, savedTransaction);
            return savedTransaction;
        } catch (AccountNotUpdatedException | InvalidFieldException e) {
            throw new TransactionNotCreatedException(e);
        }
    }

    private void processNewTransactionToAccount(String email, String accountId, Transaction savedTransaction) {
        if (!createUser.userExists(email))
            throw new UserNotFoundException();

        Account account = getAccount.getAccount(email, accountId);
        List<String> transactions_ids = account.getTransactions_ids();
        transactions_ids.add(savedTransaction.getId());
        account.updateAmount(savedTransaction.getAmount());
        account.setTransactions_ids(transactions_ids);
        updateAccount.updateAccount(email, account);
    }
}
