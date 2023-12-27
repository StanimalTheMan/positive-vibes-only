package com.jiggycode.journalentry;

import com.jiggycode.author.Author;
import com.jiggycode.author.AuthorRepository;
import com.jiggycode.exception.AuthorizationException;
import com.jiggycode.exception.DuplicateResourceException;
import com.jiggycode.exception.RequestValidationException;
import com.jiggycode.exception.ResourceNotFoundException;
import com.jiggycode.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class JournalEntryController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private SentimentService sentimentService;

    private boolean isAuthorizedAuthor(Integer authorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return authorRepository.existsAuthorByIdAndEmail(authorId, currentUsername);
    }

    @GetMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesByAuthorId(
            @PathVariable(value = "authorId") Integer authorId) {
        if (!isAuthorizedAuthor(authorId)) {
            throw new AuthorizationException("You are not authorized to view this resource.");
        }

        List<JournalEntry> journalEntries = journalEntryRepository.findByAuthorId(authorId);
        return new ResponseEntity<>(journalEntries, HttpStatus.OK);
    }

    @GetMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable(value = "id") Integer id) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find Journal Entry with id = " + id));

        // Check if the authenticated user is the author of the requested entry
        if (!isAuthorizedAuthor(journalEntry.getAuthor().getId())) {
            throw new AuthorizationException("You are not authorized to view this resource.");
        }

        return new ResponseEntity<>(journalEntry, HttpStatus.OK);
    }

    @PostMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<JournalEntry> createJournalEntry(
            @PathVariable(value = "authorId") Integer authorId,
            @RequestBody JournalEntry journalEntryRequest)
    {
        if (!isAuthorizedAuthor(authorId)) {
            throw new AuthorizationException("You are not authorized to create a journal entry for this author.");
        }

        // Ensure that the provided authorId matches the author in the request
        if (journalEntryRequest.getAuthor() != null && !Objects.equals(journalEntryRequest.getAuthor().getId(), authorId)) {
            throw new RequestValidationException("Author ID in the path does not match the Author ID in the request body.");
        }

        // Set the author using the provided authorId
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id = " + authorId));

        // Set other fields in the journalEntryRequest
        DetectSentimentResponse sentimentResponse;
        try {
            sentimentResponse = sentimentService.detectSentimentResponse(journalEntryRequest.getContent());
        } catch (Exception e) {
            throw new RuntimeException("Server error");
        }

        if (sentimentResponse.sentimentScore().positive() > 0.5f) {
            journalEntryRequest.setAuthor(author);
            journalEntryRequest.setCreationDate(journalEntryRequest.getCreationDate() != null ? journalEntryRequest.getCreationDate() : LocalDate.now());
            journalEntryRequest.setUpdatedDate(journalEntryRequest.getCreationDate() != null ? journalEntryRequest.getCreationDate() : LocalDate.now());
            journalEntryRequest.setPositiveSentimentScore(sentimentResponse.sentimentScore().positive());
            journalEntryRequest.setNegativeSentimentScore(sentimentResponse.sentimentScore().negative());
            journalEntryRequest.setNeutralSentimentScore(sentimentResponse.sentimentScore().neutral());
            journalEntryRequest.setMixedSentimentScore(sentimentResponse.sentimentScore().mixed());

            // Check for existing journal entry with the same creation date and author
            if (journalEntryRepository.existsByCreationDateAndAuthor(journalEntryRequest.getCreationDate(), author)) {
                throw new DuplicateResourceException("Journal entry with the same creation date already exists for this author.");
            }
        } else {
            throw new RuntimeException("Invalid journal entry. Try to be more positive. Current positive sentiment score is less than 0.5: " + sentimentResponse.sentimentScore().positive());
        }

        // Save the journal entry
        JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntryRequest);

        return new ResponseEntity<>(savedJournalEntry, HttpStatus.CREATED);
    }

    @PutMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable("id") Integer id, @RequestBody JournalEntryUpdateRequest updateRequest) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JournalEntryId " + id + " not found"));

        // Check if the authenticated user is the author of the requested entry
        if (!isAuthorizedAuthor(journalEntry.getAuthor().getId())) {
            throw new AuthorizationException("You are not authorized to update this resource.");
        }

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
                journalEntry.setSubject(updateRequest.subject());
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
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find journal entry with id " + id));

        // Check if the authenticated user is the author of the requested entry
        if (!isAuthorizedAuthor(journalEntry.getAuthor().getId())) {
            throw new AuthorizationException("You are not authorized to delete this resource.");
        }

        journalEntryRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<HttpStatus> deleteAllJournalEntriesOfAuthor(@PathVariable(value = "authorId") Integer authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Did not find author with id = " + authorId);
        }

        if (!isAuthorizedAuthor(authorId)) {
            throw new AuthorizationException("You are not authorized to delete journal entries for this author.");
        }

        journalEntryRepository.deleteByAuthorId(authorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
