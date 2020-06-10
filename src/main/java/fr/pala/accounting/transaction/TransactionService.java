package fr.pala.accounting.transaction;

import fr.pala.accounting.transaction.exception.TransactionNotCreatedException;
import fr.pala.accounting.transaction.model.TransactionDTO;
import fr.pala.accounting.transaction.model.TransactionModel;
import fr.pala.accounting.user.UserDAO;
import fr.pala.accounting.user.exception.UserNotFoundException;
import fr.pala.accounting.user.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final UserDAO userDAO;

    public TransactionService(TransactionDAO transactionDAO, UserDAO userDAO) {
        this.transactionDAO = transactionDAO;
        this.userDAO = userDAO;
    }

    public String registerTransaction(String email, String accountId, TransactionDTO transactionDTO) {
        UserModel user = userDAO.getUserByEmail(email);
        if(user == null) {
            throw new UserNotFoundException();
        }
        TransactionModel transaction = new TransactionModel()
                .setType(transactionDTO.getType())
                .setShop_name(transactionDTO.getShop_name())
                .setShop_address(transactionDTO.getShop_address())
                .setDate(new Date())
                .setAmount(transactionDTO.getAmount())
                .setDescription(transactionDTO.getDescription());

        transaction = transactionDAO.addTransaction(user.getUser_id(), accountId, transaction);

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

        return registerTransaction(userName, accountId, transactionDTO);
    }
}