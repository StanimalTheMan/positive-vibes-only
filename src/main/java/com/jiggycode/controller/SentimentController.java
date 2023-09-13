package com.jiggycode.controller;

import com.jiggycode.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

@RestController
public class SentimentController {

    @Autowired
    private SentimentService sentimentService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeSentiment(@RequestBody String text) {
        try {
            DetectSentimentResponse sentimentResponse = sentimentService.detectSentimentResponse(text);
            String sentiment = sentimentResponse.sentiment().toString();
            return ResponseEntity.ok("Sentiment: " + sentiment);
        } catch (Exception e) {
            // Handle exceptions and error responses here
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}
