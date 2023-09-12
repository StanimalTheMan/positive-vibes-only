package com.jiggycode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;

@Service
public class SentimentServiceImpl implements SentimentService {

    @Autowired
    private ComprehendClient comprehendClient;

    @Override
    public DetectSentimentResponse detectSentimentResponse(String text) {
        DetectSentimentRequest detectSentimentReqeust =
                DetectSentimentRequest.builder().text(text)
                        .languageCode(LanguageCode.EN)
                        .build();
        DetectSentimentResponse detectSentimentResponse = comprehendClient
                .detectSentiment(detectSentimentReqeust);

        return detectSentimentResponse;
    }
}
