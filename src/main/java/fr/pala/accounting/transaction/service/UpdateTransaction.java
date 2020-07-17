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

import java.util.Date;

@Service
public class UpdateTransaction {
    private final TransactionDAO transactionDAO;
    private final CreateUser createUser;
    private final GetAccount getAccount;
    private final UpdateAccount updateAccount;

    public UpdateTransaction(TransactionDAO transactionDAO, CreateUser createUser, GetAccount getAccount, UpdateAccount updateAccount) {
        this.transactionDAO = transactionDAO;
        this.createUser = createUser;
        this.getAccount = getAccount;
        this.updateAccount = updateAccount;
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

    private void processUpdatedTransactionInAccount(String email, String accountId, Transaction updatedTransaction) {
        if (!createUser.userExists(email))
            throw new UserNotFoundException();

        Account account = getAccount.getAccount(email, accountId);
        account.updateAmount(updatedTransaction.getAmount());
        updateAccount.updateAccount(email, account);
    }
}
