package fr.pala.accounting.transaction;

import fr.pala.accounting.dao.TransactionDAO;
import fr.pala.accounting.ocr_space.OCRSpaceService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

import static fr.pala.accounting.utils.file.downloadUtils.downloadImage;

@RestController
@RequestMapping("/transaction")
public class TransactionController implements ErrorController {



    final TransactionDAO transactionDAO;
    final OCRSpaceService ocrSpaceService;

    private static final String PATH = "/error";

    public TransactionController(TransactionDAO transactionDAO, OCRSpaceService ocrSpaceService) {
        this.transactionDAO = transactionDAO;
        this.ocrSpaceService = ocrSpaceService;
    }

    @GetMapping
    public String index() {
        return "transaction";
    }

    @RequestMapping(value = PATH)
    public String error() {
        return "Error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file) {
        Path filePath = downloadImage(file);
        String result = ocrSpaceService.uploadAndFetchResult(filePath);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
