package com.example.EmailAI.Controller;

import com.example.EmailAI.Entity.EmailRequest;
import com.example.EmailAI.Services.EmailGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor


//@CrossOrigin(origins = "https://mail.google.com",allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST})
@CrossOrigin(origins = "https://smartaimail.netlify.app/",allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST})
public class EmailGenerateController {

    private final EmailGeneratorService emailGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
        String response = emailGeneratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }
}
