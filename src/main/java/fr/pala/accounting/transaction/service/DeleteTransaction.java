package fr.pala.accounting.transaction.service;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.GetAccount;
import fr.pala.accounting.account.service.UpdateAccount;
import fr.pala.accounting.account.service.exception.AccountNotUpdatedException;
import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.transaction.service.exception.TransactionNotUpdatedException;
import fr.pala.accounting.user.service.CreateUser;
import fr.pala.accounting.user.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteTransaction {
    private final TransactionDAO transactionDAO;
    private final CreateUser createUser;
    private final GetAccount getAccount;
    private final UpdateAccount updateAccount;

    public DeleteTransaction(TransactionDAO transactionDAO, CreateUser createUser, GetAccount getAccount, UpdateAccount updateAccount) {
        this.transactionDAO = transactionDAO;
        this.createUser = createUser;
        this.getAccount = getAccount;
        this.updateAccount = updateAccount;
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

    private void processDeletedTransactionInAccount(String email, String accountId, Transaction deletedTransaction) {
        if (!createUser.userExists(email))
            throw new UserNotFoundException();

        Account account = getAccount.getAccount(email, accountId);
        account.updateAmount(-deletedTransaction.getAmount());
        List<String> transactions_ids = account.getTransactions_ids();

        for (int i = 0; i < transactions_ids.size(); i++) {
            if(transactions_ids.get(i).equals(deletedTransaction.getId())){
                transactions_ids.remove(i);
                break;
            }
        }
        account.setTransactions_ids(transactions_ids);
        updateAccount.updateAccount(email, account);
    }
}
