package fr.pala.accounting.transaction.infrastructure.controller;

import fr.pala.accounting.transaction.service.TransactionService;
import fr.pala.accounting.utils.ticket_scan.OCRSpaceScanTicket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.security.Principal;

import static fr.pala.accounting.utils.file.downloadUtils.downloadImage;

@RestController
@RequestMapping("/account/{accountId}/transaction/")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public String index() {
        return "transaction";
    }


    @RequestMapping("/addTransaction")
    public String addTransaction(Principal principal, String account_id, TransactionDTO transactionDTO) {
        transactionService.createTransaction(principal.getName(),
                account_id,
                transactionDTO.getType(),
                transactionDTO.getShop_name(),
                transactionDTO.getShop_address(),
                transactionDTO.getAmount(),
                transactionDTO.getDescription());
        // TransactionModel transaction = new TransactionModel("1", "Restaurant", "McDo", "Clichy", new Date(), amount, "test");
        // transactionService.addTransaction(user_id, account_id, transaction);
        return "OK";
    }

    @RequestMapping("/updateTransaction")
    public String updateTransaction(Principal principal, String account_id, Double amount) {
        // TransactionModel transaction = new TransactionModel("1", "Restaurant", "McDo", "Clichy", new Date(), amount, "test");
        // transactionService.addTransaction(user_id, account_id, transaction);
        return "OK";
    }

    @RequestMapping("/filter")
    public String filter(Principal principal, String type) {
        // filtrer par montant get dans transaction
        // id_user dans le token, parser tous les comptes, toutes les transactions sup à tant ou inf à tant
        // transaction/id_account qui va aller
        return "OK";
    }

    @PostMapping("/scan")
    public ResponseEntity<String> singleFileUpload(Principal principal, @PathVariable("accountId") String accountId, @RequestParam("file") MultipartFile file) {
        Path filePath = downloadImage(file);
        String uploadResult = OCRSpaceScanTicket.uploadAndFetchResult(filePath);
        String transactionId = transactionService.registerScanTransaction(principal.getName()
                ,"None", "None", 10.0, "Unknown",
                accountId); // TODO: Scan infos of uploadResult
        return new ResponseEntity<>(transactionId, HttpStatus.OK);
    }

}
