package fr.pala.accounting.transaction.service;

import fr.pala.accounting.transaction.domain.model.InvalidFieldException;
import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.infrastructure.dao.TransactionDAO;
import fr.pala.accounting.transaction.service.exception.TransactionNotFetchedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTransactionsOfAccount {
    private final TransactionDAO transactionDAO;

    public GetAllTransactionsOfAccount(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public List<Transaction> getAllTransactionsOfAccount(String email, String account_id) {
        try {
            return transactionDAO.getAllTransactionsOfAccount(email, account_id);
        } catch (InvalidFieldException e) {
            throw new TransactionNotFetchedException(e);
        }
    }
}
