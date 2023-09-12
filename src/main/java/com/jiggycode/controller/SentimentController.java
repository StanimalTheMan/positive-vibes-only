package com.jiggycode.controller;

import com.jiggycode.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

@RestController
public class SentimentController {

    @Autowired
    private SentimentService sentimentService;

    @GetMapping("/get-sentiment")
    public DetectSentimentResponse getSentimentData(@RequestBody String text) {
        return sentimentService.detectSentimentResponse(text);
    }
}
