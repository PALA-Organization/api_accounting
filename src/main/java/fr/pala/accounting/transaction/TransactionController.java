package fr.pala.accounting.transaction;

import fr.pala.accounting.ocr_space.OCRSpaceService;
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
    private final OCRSpaceService ocrSpaceService;
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService, OCRSpaceService ocrSpaceService) {
        this.ocrSpaceService = ocrSpaceService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public String index() {
        return "transaction";
    }


    @RequestMapping("/addTransaction")
    public String addTransaction(String user_id, String account_id, Double amount) {
        // TransactionModel transaction = new TransactionModel("1", "Restaurant", "McDo", "Clichy", new Date(), amount, "test");
        // transactionService.addTransaction(user_id, account_id, transaction);
        return "OK";
    }

    @RequestMapping("/updateTransaction")
    public String updateTransaction(String user_id, String account_id, Double amount) {
        // TransactionModel transaction = new TransactionModel("1", "Restaurant", "McDo", "Clichy", new Date(), amount, "test");
        // transactionService.addTransaction(user_id, account_id, transaction);
        return "OK";
    }

    @RequestMapping("/filter")
    public String filter(String user_id, String type) {
        // filtrer par montant get dans transaction
        // id_user dans le token, parser tous les comptes, toutes les transactions sup à tant ou inf à tant
        // transaction/id_account qui va aller
        return "OK";
    }

    @PostMapping("/scan")
    public ResponseEntity<String> singleFileUpload(@PathVariable("accountId") String accountId, @RequestParam("file") MultipartFile file, Principal principal) {
        Path filePath = downloadImage(file);
        String uploadResult = ocrSpaceService.uploadAndFetchResult(filePath);
        String transactionId = transactionService.registerScanTransaction(principal.getName()
                ,"None", "None", 10.0, "Unknown",
                accountId); // TODO: Scan infos of uploadResult
        return new ResponseEntity<>(transactionId, HttpStatus.OK);
    }

}
