package com.example.EmailAI.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailRequest {
    private String emailContent;
    private String tone;
}
