package fr.pala.accounting.transaction.infrastructure.controller;

import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.service.CreateTransaction;
import fr.pala.accounting.transaction.service.DeleteTransaction;
import fr.pala.accounting.transaction.service.GetAllTransactionsOfAccount;
import fr.pala.accounting.transaction.service.UpdateTransaction;
import fr.pala.accounting.utils.ticket_scan.OCRSpaceScanTicket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

import static fr.pala.accounting.utils.file.downloadUtils.downloadImage;

@RestController
@RequestMapping("/account/{accountId}/transaction/")
public class TransactionController {
    private final GetAllTransactionsOfAccount getAllTransactionsOfAccount;
    private final CreateTransaction createTransaction;
    private final DeleteTransaction deleteTransaction;
    private final UpdateTransaction updateTransaction;

    public TransactionController(GetAllTransactionsOfAccount getAllTransactionsOfAccount, CreateTransaction createTransaction, DeleteTransaction deleteTransaction, UpdateTransaction updateTransaction) {
        this.getAllTransactionsOfAccount = getAllTransactionsOfAccount;
        this.createTransaction = createTransaction;
        this.deleteTransaction = deleteTransaction;
        this.updateTransaction = updateTransaction;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactionsOfAccount(Principal principal, @PathVariable("accountId") @NotEmpty(message = "account_id must not be empty") String account_id) {
        List<Transaction> transactionList = getAllTransactionsOfAccount.getAllTransactionsOfAccount(principal.getName(), account_id);
        return ResponseEntity.ok(transactionList);
    }

    @PostMapping
    public ResponseEntity<String> addTransactionToAccount(Principal principal, @PathVariable("accountId") @NotEmpty(message = "account_id must not be empty")
            String account_id, @RequestBody @Valid TransactionDTO transactionDTO) {
        String newTransactionId = createTransaction.createTransaction(principal.getName(),
                account_id,
                transactionDTO.getType(),
                transactionDTO.getShop_name(),
                transactionDTO.getShop_address(),
                transactionDTO.getAmount(),
                transactionDTO.getDescription()).getId();

        return ResponseEntity.ok(newTransactionId);
    }

    @PostMapping("/scan")
    public ResponseEntity<String> addScanTransactionToAccount(Principal principal, @PathVariable("accountId") String accountId, @RequestParam("file") MultipartFile file) {
        Path filePath = downloadImage(file);
        String uploadResult = OCRSpaceScanTicket.uploadAndFetchResult(filePath);
        String transactionId = createTransaction.createTransaction(principal.getName(),
                accountId,
                "Ticket",
                "None",
                "None",
                0.0,
                uploadResult).getId();

        return new ResponseEntity<>(transactionId, HttpStatus.OK);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<String> updateTransaction(
            Principal principal,
            @PathVariable("accountId") @NotEmpty(message = "accountId must not be empty") String accountId,
            @PathVariable("transactionId") @NotEmpty(message = "transactionId must not be empty") String transactionId,
            @RequestBody @Valid TransactionDTO transactionDTO) {
        String updatedTransactionId = updateTransaction.updateTransaction(principal.getName(),
                accountId,
                transactionId,
                transactionDTO.getType(),
                transactionDTO.getShop_name(),
                transactionDTO.getShop_address(),
                transactionDTO.getAmount(),
                transactionDTO.getDescription()).getId();
        return ResponseEntity.ok(updatedTransactionId);
    }


    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(
            Principal principal,
            @PathVariable("accountId") @NotEmpty(message = "accountId must not be empty") String accountId,
            @PathVariable("transactionId") @NotEmpty(message = "transactionId must not be empty") String transactionId) {
        deleteTransaction.deleteTransaction(principal.getName(), accountId, transactionId);
        return ResponseEntity.ok().build();
    }
}
