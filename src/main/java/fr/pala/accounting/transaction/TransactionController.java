package fr.pala.accounting.transaction;

import fr.pala.accounting.dao.TransactionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController implements ErrorController {

    private final String UPLOAD_FOLDER = "./Download/";
    @Value("${ocr.token}")
    private String ocrToken;

    @Autowired
    TransactionDAO transactionDAO;

    private static final String PATH = "/error";

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
    public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {

        String url = "https://api.ocr.space/parse/image?language=fre";
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.UNAUTHORIZED);
        }

        Path filePath;
        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();

            File directory = new File(UPLOAD_FOLDER);
            if(!directory.exists()) {
                directory.mkdir();
            }

            filePath = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(filePath, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occured during the download of your file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", ocrToken);
        headers.set("filetype", "PNG");

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(filePath)/*new ByteArrayResource(file.getBytes())*/);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        Map<String, Object> final_response = response.getBody();
        ArrayList<Map<String, Object>> ParsedResults = (ArrayList) final_response.get("ParsedResults");

        Map<String, Object> parsedResult = ParsedResults.get(0);

        String result_ocr = (String) parsedResult.get("ParsedText");

        return new ResponseEntity<>(result_ocr, HttpStatus.OK);
    }


}
