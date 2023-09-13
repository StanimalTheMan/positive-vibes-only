package com.jiggycode.journalentry;

import com.jiggycode.author.AuthorRepository;
import com.jiggycode.exception.ResourceNotFoundException;
import com.jiggycode.service.SentimentService;
import jakarta.annotation.Resource;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class JournalEntryController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private SentimentService sentimentService;

    @GetMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesByAuthorId(@PathVariable(value = "authorId") Integer authorId) {
        if (!authorRepository.existsAuthorById(authorId)) {
            throw new ResourceNotFoundException("Did not find Author with id = " + authorId);
        }

        List<JournalEntry> journalEntries = journalEntryRepository.findByAuthorId(authorId);
        return new ResponseEntity<>(journalEntries, HttpStatus.OK);
    }

    @PostMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<JournalEntry> createJournalEntry(@PathVariable(value = "authorId") Integer authorId, @RequestBody JournalEntry journalEntryRequest)
    {
        JournalEntry journalEntry = authorRepository.findById(authorId).map(author -> {
            DetectSentimentResponse sentimentResponse;
            try {
                sentimentResponse = sentimentService.detectSentimentResponse(journalEntryRequest.getContent());
            } catch (Exception e) {
                // Handle exceptions and error responses here
                throw new RuntimeException("servor error");
            }
            if (sentimentResponse.sentimentScore().positive() > 0.5f) {
                journalEntryRequest.setAuthor(author);
                journalEntryRequest.setCreationDate(LocalDate.now());
                journalEntryRequest.setPositiveSentimentScore(sentimentResponse.sentimentScore().positive());
                journalEntryRequest.setNegativeSentimentScore(sentimentResponse.sentimentScore().negative());
                journalEntryRequest.setNeutralSentimentScore(sentimentResponse.sentimentScore().neutral());
                journalEntryRequest.setMixedSentimentScore(sentimentResponse.sentimentScore().mixed());
            } else {
                throw new RuntimeException("Invalid journal entry.  Try to be more positive.");
            }
            return journalEntryRepository.save(journalEntryRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Did not find Author with id = " + authorId));
        return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
    }
}
