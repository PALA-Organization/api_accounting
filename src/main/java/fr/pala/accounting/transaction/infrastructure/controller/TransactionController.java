package fr.pala.accounting.transaction.infrastructure.controller;

import fr.pala.accounting.transaction.domain.model.Transaction;
import fr.pala.accounting.transaction.service.TransactionService;
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
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/filter")
    public String filter(Principal principal, String type) {
        // filtrer par montant get dans transaction
        // id_user dans le token, parser tous les comptes, toutes les transactions sup à tant ou inf à tant
        // transaction/id_account qui va aller
        return "OK";
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactionsOfAccount(Principal principal, @PathVariable("accountId") @NotEmpty(message = "account_id must not be empty") String account_id) {
        List<Transaction> transactionList = transactionService.getAllTransactionsOfAccount(principal.getName(), account_id);
        return ResponseEntity.ok(transactionList);
    }

    @PostMapping
    public ResponseEntity<String> addTransactionToAccount(Principal principal, @PathVariable("accountId") @NotEmpty(message = "account_id must not be empty")
            String account_id, @RequestBody @Valid TransactionDTO transactionDTO) {
        String newTransactionId = transactionService.createTransaction(principal.getName(),
                account_id,
                transactionDTO.getType(),
                transactionDTO.getShop_name(),
                transactionDTO.getShop_address(),
                transactionDTO.getAmount(),
                transactionDTO.getDescription()).getId();
        // TransactionModel transaction = new TransactionModel("1", "Restaurant", "McDo", "Clichy", new Date(), amount, "test");
        // transactionService.addTransaction(user_id, account_id, transaction);
        return ResponseEntity.ok(newTransactionId);
    }

    @PostMapping("/scan")
    public ResponseEntity<String> addScanTransactionToAccount(Principal principal, @PathVariable("accountId") String accountId, @RequestParam("file") MultipartFile file) {
        Path filePath = downloadImage(file);
        String uploadResult = OCRSpaceScanTicket.uploadAndFetchResult(filePath);
        String transactionId = transactionService.registerScanTransaction(principal.getName(), accountId
                ,"None", "None", 10.0, uploadResult).getId(); // TODO: Scan infos of uploadResult
        return new ResponseEntity<>(transactionId, HttpStatus.OK);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<String> updateTransaction(
            Principal principal,
            @PathVariable("accountId") @NotEmpty(message = "accountId must not be empty") String accountId,
            @PathVariable("transactionId") @NotEmpty(message = "transactionId must not be empty") String transactionId,
            @RequestBody @Valid TransactionDTO transactionDTO) {
        String updatedTransactionId = transactionService.updateTransaction(principal.getName(),
                accountId,
                transactionId,
                transactionDTO.getType(),
                transactionDTO.getShop_name(),
                transactionDTO.getShop_address(),
                transactionDTO.getAmount(),
                transactionDTO.getDescription()).getId();
        return ResponseEntity.ok(updatedTransactionId);
    }
}
