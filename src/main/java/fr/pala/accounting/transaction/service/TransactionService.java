package fr.pala.accounting.transaction.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.AccountService;
import fr.pala.accounting.account.service.exception.AccountNotUpdatedException;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.transaction.service.exception.TransactionNotCreatedException;
import fr.pala.accounting.transaction.service.exception.TransactionNotFetchedException;
import fr.pala.accounting.transaction.service.exception.TransactionNotUpdatedException;
import fr.pala.accounting.user.infrastructure.dao.UserDAO;
import fr.pala.accounting.user.service.UserService;
import fr.pala.accounting.user.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final AccountService accountService;
    private final UserService userService;
    private final UserDAO userDAO;

    public TransactionService(TransactionDAO transactionDAO, AccountService accountService, UserService userService, UserDAO userDAO) {
        this.transactionDAO = transactionDAO;
        this.accountService = accountService;
        this.userService = userService;
        this.userDAO = userDAO;
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

    public List<Transaction> getAllTransactionsOfAccount(String email, String account_id) {
        try {
            return transactionDAO.getAllTransactionsOfAccount(email, account_id);
        } catch (InvalidFieldException e) {
            throw new TransactionNotFetchedException(e);
        }
    }

    public Transaction updateTransaction(String email, String accountId, String transactionId, String type, String shop_name, String shop_address, Double amount, String description) {
        Transaction transaction = new Transaction(transactionId, type, shop_name, shop_address, new Date(), amount, description);

        try {
            Transaction updatedTransaction = transactionDAO.updateTransaction(transaction);
            processUpdatedTransactionInAccount(email, accountId, updatedTransaction);
            return updatedTransaction;
        } catch (AccountNotUpdatedException | InvalidFieldException e) {
            throw new TransactionNotUpdatedException(e);
        }
    }

    public void deleteTransaction(String email, String accountId, String transactionId) {
        try {
            Transaction transactionToDelete = transactionDAO.getTransaction(transactionId);
            transactionDAO.deleteTransaction(transactionToDelete);
            processDeletedTransactionInAccount(email, accountId, transactionToDelete);
        } catch (AccountNotUpdatedException | InvalidFieldException e) {
            throw new TransactionNotUpdatedException(e);
        }
    }

    private void processNewTransactionToAccount(String email, String accountId, Transaction savedTransaction) {
        if (!userService.userExists(email))
            throw new UserNotFoundException();

        Account account = accountService.getAccount(email, accountId);
        List<String> transactions_ids = account.getTransactions_ids();
        transactions_ids.add(savedTransaction.getId());
        account.updateAmount(savedTransaction.getAmount());
        account.setTransactions_ids(transactions_ids);
        accountService.updateAccount(email, account);
    }

    private void processUpdatedTransactionInAccount(String email, String accountId, Transaction updatedTransaction) {
        if (!userService.userExists(email))
            throw new UserNotFoundException();

        Account account = accountService.getAccount(email, accountId);
        account.updateAmount(updatedTransaction.getAmount());
        accountService.updateAccount(email, account);
    }

    private void processDeletedTransactionInAccount(String email, String accountId, Transaction deletedTransaction) {
        if (!userService.userExists(email))
            throw new UserNotFoundException();

        Account account = accountService.getAccount(email, accountId);
        account.updateAmount(-deletedTransaction.getAmount());
        List<String> transactions_ids = account.getTransactions_ids();

        for (int i = 0; i < transactions_ids.size(); i++) {
            if(transactions_ids.get(i).equals(deletedTransaction.getId())){
                transactions_ids.remove(i);
                break;
            }
        }
        account.setTransactions_ids(transactions_ids);
        accountService.updateAccount(email, account);
    }




}
