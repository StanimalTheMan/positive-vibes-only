package com.jiggycode.service;

import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

public interface SentimentService {

    DetectSentimentResponse detectSentimentResponse(String text);
}
