package com.jiggycode.journalentry;
import com.jiggycode.author.Author;
import com.jiggycode.author.AuthorRepository;
import com.jiggycode.exception.AuthorizationException;
import com.jiggycode.exception.DuplicateResourceException;
import com.jiggycode.exception.RequestValidationException;
import com.jiggycode.exception.ResourceNotFoundException;
import com.jiggycode.service.SentimentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class JournalEntryService {

    private final AuthorRepository authorRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final SentimentService sentimentService;

    public JournalEntryService(AuthorRepository authorRepository, JournalEntryRepository journalEntryRepository, SentimentService sentimentService) {
        this.authorRepository = authorRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.sentimentService = sentimentService;
    }

    public List<JournalEntry> getAllJournalEntriesByAuthorId(Integer authorId) {
        if (!isAuthorizedAuthor(authorId)) {
            throw new AuthorizationException("You are not authorized to view journal entries for this author.");
        }

        return journalEntryRepository.findByAuthorId(authorId);
    }

    public JournalEntry getJournalEntryById(Integer id) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find Journal Entry with id = " + id));

        if (!isAuthorizedAuthor(journalEntry.getAuthor().getId())) {
            throw new AuthorizationException("You are not authorized to view this resource.");
        }

        return journalEntry;
    }

    public JournalEntry createJournalEntry(Integer authorId, JournalEntry journalEntryRequest) {
        if (!isAuthorizedAuthor(authorId)) {
            throw new AuthorizationException("You are not authorized to create a journal entry for this author.");
        }

        validateAuthorId(authorId, journalEntryRequest);

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id = " + authorId));

        setJournalEntryFields(journalEntryRequest, author);

        if (journalEntryRepository.existsByCreationDateAndAuthor(journalEntryRequest.getCreationDate(), author)) {
            throw new DuplicateResourceException("Journal entry with the same creation date already exists for this author.");
        }

        return journalEntryRepository.save(journalEntryRequest);
    }

    private void validateAuthorId(Integer authorId, JournalEntry journalEntryRequest) {
        if (journalEntryRequest.getAuthor() != null && !Objects.equals(journalEntryRequest.getAuthor().getId(), authorId)) {
            throw new RequestValidationException("Author ID in the path does not match the Author ID in the request body.");
        }
    }

    private void setJournalEntryFields(JournalEntry journalEntryRequest, Author author) {
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
        } else {
            throw new RuntimeException("Invalid journal entry. Try to be more positive. Current positive sentiment score is less than 0.5: " + sentimentResponse.sentimentScore().positive());
        }
    }

    public JournalEntry updateJournalEntry(Integer id, JournalEntryUpdateRequest updateRequest) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JournalEntryId " + id + " not found"));

        if (!isAuthorizedAuthor(journalEntry.getAuthor().getId())) {
            throw new AuthorizationException("You are not authorized to update this resource.");
        }

        boolean changes = false;

        if (updateRequest.content() != null && !updateRequest.content().equals(journalEntry.getContent())) {
            updateJournalEntryContent(journalEntry, updateRequest);
            changes = true;
        }

        if (updateRequest.subject() != null && !updateRequest.subject().equals(journalEntry.getSubject())) {
            journalEntry.setSubject(updateRequest.subject());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found");
        }

        return journalEntryRepository.save(journalEntry);
    }

    private void updateJournalEntryContent(JournalEntry journalEntry, JournalEntryUpdateRequest updateRequest) {
        DetectSentimentResponse sentimentResponse;
        try {
            sentimentResponse = sentimentService.detectSentimentResponse(updateRequest.content());
        } catch (Exception e) {
            throw new RuntimeException("Server error");
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
            throw new RuntimeException("Invalid journal entry. Try to be more positive. Current positive sentiment score is less than 0.5: " + sentimentResponse.sentimentScore().positive());
        }
    }

    public void deleteJournalEntry(Integer id) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Did not find journal entry with id " + id));

        if (!isAuthorizedAuthor(journalEntry.getAuthor().getId())) {
            throw new AuthorizationException("You are not authorized to delete this resource.");
        }

        journalEntryRepository.deleteById(id);
    }

    public void deleteAllJournalEntriesOfAuthor(Integer authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Did not find author with id = " + authorId);
        }

        if (!isAuthorizedAuthor(authorId)) {
            throw new AuthorizationException("You are not authorized to delete journal entries for this author.");
        }

        journalEntryRepository.deleteByAuthorId(authorId);
    }

    boolean isAuthorizedAuthor(Integer authorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return authorRepository.existsAuthorByIdAndEmail(authorId, currentUsername);
    }
}
