package com.jiggycode.journalentry;

import com.jiggycode.exception.DuplicateResourceException;
import com.jiggycode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesByAuthorId(
            @PathVariable(value = "authorId") Integer authorId) {
        List<JournalEntry> journalEntries = journalEntryService.getAllJournalEntriesByAuthorId(authorId);
        return new ResponseEntity<>(journalEntries, HttpStatus.OK);
    }

    @GetMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable(value = "id") Integer id) {
        JournalEntry journalEntry = journalEntryService.getJournalEntryById(id);
        return new ResponseEntity<>(journalEntry, HttpStatus.OK);
    }

    @PostMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<JournalEntry> createJournalEntry(
            @PathVariable(value = "authorId") Integer authorId,
            @RequestBody JournalEntry journalEntryRequest)
    {
        JournalEntry createdJournalEntry = journalEntryService.createJournalEntry(authorId, journalEntryRequest);
        return new ResponseEntity<>(createdJournalEntry, HttpStatus.CREATED);
    }

    @PutMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable("id") Integer id, @RequestBody JournalEntryUpdateRequest updateRequest) {
        JournalEntry updatedJournalEntry = journalEntryService.updateJournalEntry(id, updateRequest);
        return new ResponseEntity<>(updatedJournalEntry, HttpStatus.OK);
    }

    @DeleteMapping("/journal-entries/{id}")
    public ResponseEntity<HttpStatus> deleteJournalEntry(@PathVariable("id") Integer id) {
        journalEntryService.deleteJournalEntry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/authors/{authorId}/journal-entries")
    public ResponseEntity<HttpStatus> deleteAllJournalEntriesOfAuthor(@PathVariable(value = "authorId") Integer authorId) {
        journalEntryService.deleteAllJournalEntriesOfAuthor(authorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Exception handler for ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Exception handler for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
