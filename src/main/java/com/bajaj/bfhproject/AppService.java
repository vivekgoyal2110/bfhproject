package com.bajaj.bfhproject;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppService {

    public void start() {
        RestTemplate restTemplate = new RestTemplate();

        
        WebhookRequest request = new WebhookRequest("John Doe", "REG12347", "john@example.com");

        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
                "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA",
                request,
                WebhookResponse.class
        );

        WebhookResponse data = response.getBody();
        if (data == null) {
            System.out.println("Failed to get webhook.");
            return;
        }

        String webhook = data.getWebhook();
        String token = data.getAccessToken();

        String sqlQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME,\n" +
                "    (\n" +
                "        SELECT COUNT(*) \n" +
                "        FROM EMPLOYEE e2 \n" +
                "        WHERE e2.DEPARTMENT = e1.DEPARTMENT \n" +
                "          AND e2.DOB > e1.DOB\n" +
                "    ) AS YOUNGER_EMPLOYEES_COUNT\n" +
                "FROM EMPLOYEE e1\n" +
                "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID\n" +
                "ORDER BY e1.EMP_ID DESC;";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", sqlQuery);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> finalResponse = restTemplate.postForEntity(
                webhook,
                entity,
                String.class
        );

        System.out.println("Submission Response: " + finalResponse.getBody());
    }
}
