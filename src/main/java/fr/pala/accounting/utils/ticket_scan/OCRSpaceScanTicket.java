package fr.pala.accounting.utils.ticket_scan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class OCRSpaceScanTicket {
    @Value("${ocr.token}")
    private static String ocrToken;

    public static String uploadAndFetchResult(Path filePath) {
        String url = "https://api.ocr.space/parse/image?language=fre";

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

        return (String) parsedResult.get("ParsedText");
    }
}
