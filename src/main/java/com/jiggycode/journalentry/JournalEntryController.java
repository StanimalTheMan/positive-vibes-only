package com.jiggycode.journalentry;

import com.jiggycode.author.AuthorRepository;
import com.jiggycode.exception.RequestValidationException;
import com.jiggycode.exception.ResourceNotFoundException;
import com.jiggycode.service.SentimentService;
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

    @GetMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable(value = "id") Integer id) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find Journal Entry with id = " + id));

        return new ResponseEntity<>(journalEntry, HttpStatus.OK);
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
                throw new RuntimeException("server error");
            }
            if (sentimentResponse.sentimentScore().positive() > 0.5f) {
                journalEntryRequest.setAuthor(author);
                journalEntryRequest.setCreationDate(journalEntryRequest.getCreationDate() != null ? journalEntryRequest.getCreationDate() : LocalDate.now());
                journalEntryRequest.setUpdatedDate(journalEntryRequest.getCreationDate() != null ? journalEntryRequest.getCreationDate() : LocalDate.now());
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

    @PutMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable("id") Integer id, @RequestBody JournalEntryUpdateRequest updateRequest) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JournalEntryId " + id + " not found"));

        boolean changes = false;

        if (updateRequest.content() != null && !updateRequest.content().equals(journalEntry.getContent())) {
            DetectSentimentResponse sentimentResponse;
            try {
                sentimentResponse = sentimentService.detectSentimentResponse(updateRequest.content());
            } catch (Exception e) {
                // Handle exceptions and error responses here
                throw new RuntimeException("server error");
            }
            if (sentimentResponse.sentimentScore().positive() > 0.5f) {
                journalEntry.setContent(updateRequest.content());
                journalEntry.setUpdatedDate(LocalDate.now());
                journalEntry.setPositiveSentimentScore(sentimentResponse.sentimentScore().positive());
                journalEntry.setNegativeSentimentScore(sentimentResponse.sentimentScore().negative());
                journalEntry.setNeutralSentimentScore(sentimentResponse.sentimentScore().neutral());
                journalEntry.setMixedSentimentScore(sentimentResponse.sentimentScore().mixed());
            } else {
                throw new RuntimeException("Invalid journal entry.  Try to be more positive.  Current positive sentiment score is less than 0.5: " + sentimentResponse.sentimentScore().positive());
            }
            changes = true;
        }

        if (updateRequest.subject() != null && !updateRequest.subject().equals(journalEntry.getSubject())) {
            journalEntry.setSubject(updateRequest.subject());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        return new ResponseEntity<>(journalEntryRepository.save(journalEntry), HttpStatus.OK);
    }

    @DeleteMapping("/journal-entries/{id}")
    public ResponseEntity<HttpStatus> deleteJournalEntry(@PathVariable("id") Integer id) {
        if (!journalEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Did not find journal entry with id " + id);
        }

        journalEntryRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<HttpStatus> deleteAllJournalEntriesOfAuthor(@PathVariable(value = "authorId") Integer authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Did not find author with id = " + authorId);
        }

        journalEntryRepository.deleteByAuthorId(authorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
