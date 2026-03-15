package com.example.EmailAI.Services;

import com.example.EmailAI.Entity.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;


@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    private final String apiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder,
                                 @Value("${gemini.api.url}") String baseUrl,
                                 @Value("${gemini.api.key}") String geminiApiKey) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = geminiApiKey;
    }

    public String generateEmailReply(EmailRequest emailRequest){
        // Build Prompt
        String prompt = buildPrompt(emailRequest);
        // Prep. Raw JSON body
        String requestBody = String.format("""
               {
                    "contents": [
                      {
                        "parts": [
                          {
                            "text": "%s"
                          }
                        ]
                      }
                    ]
               }""",prompt);
        // Send RequestBody
            String response = webClient.post().uri(uriBuilder -> uriBuilder.path("/v1beta/models/gemini-3-flash-preview:generateContent").build())
                    .header("x-goog-api-key",apiKey)
                    .header("Content-Type","application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
//                    .timeout(Duration.ofSeconds(10))
                    .block();
        // Extract Response
        return ExtractResponse(response);
    }

    private String ExtractResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing API response", e);
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email: ");
        if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()){
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone to Generate the reply.");
        }
        prompt.append("Original Email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
