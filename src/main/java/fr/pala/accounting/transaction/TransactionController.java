package fr.pala.accounting.transaction;

import fr.pala.accounting.ocr_space.OCRSpaceService;
import fr.pala.accounting.user.dao.UserDAO;
import fr.pala.accounting.user.model.UserModel;
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
    final OCRSpaceService ocrSpaceService;
    final TransactionService transactionService;
    private static final String PATH = "/error";

    public TransactionController(TransactionService transactionService, OCRSpaceService ocrSpaceService) {
        this.transactionService = transactionService;
        this.ocrSpaceService = ocrSpaceService;
    }

    @GetMapping
    public String index() {
        return "transaction";
    }

    @PostMapping("/scan")
    public ResponseEntity<String> singleFileUpload(@PathVariable("accountId") String accountId, @RequestParam("file") MultipartFile file, Principal principal) {
        Path filePath = downloadImage(file);
        String uploadResult = ocrSpaceService.uploadAndFetchResult(filePath);
        String transactionId = transactionService.RegisterScanTransaction(principal.getName()
                ,"None", "None", 10.0, "Unknown",
                accountId);
        return new ResponseEntity<>(transactionId, HttpStatus.OK);
    }

}
