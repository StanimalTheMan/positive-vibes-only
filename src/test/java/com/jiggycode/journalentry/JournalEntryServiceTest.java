package com.jiggycode.journalentry;
import com.jiggycode.author.AuthorRepository;
import com.jiggycode.service.SentimentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.jiggycode.author.Author;
import com.jiggycode.journalentry.JournalEntry;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;
import software.amazon.awssdk.services.comprehend.model.SentimentScore;

import java.time.LocalDate;
import java.util.Optional;


public class JournalEntryServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private SentimentService sentimentService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JournalEntryService journalEntryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Mock the SecurityContextHolder and set the Authentication object
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testIsAuthorizedAuthor_AuthorizedUser() {
        // Mock the behavior of authentication.getName() to return the expected username
        when(authentication.getName()).thenReturn("username");

        // Mock the behavior of authorRepository.existsAuthorByIdAndEmail() to return true
        when(authorRepository.existsAuthorByIdAndEmail(anyInt(), eq("username"))).thenReturn(true);

        // Invoke the method under test
        boolean result = journalEntryService.isAuthorizedAuthor(1);

        // Verify that the correct method was called with the correct argument
        verify(authorRepository).existsAuthorByIdAndEmail(1, "username");

        // Verify the result
        assertTrue(result, "The user should be authorized.");
    }

    @Test
    public void testCreateJournalEntry_WithAuthorizedUser() {
        // Mock the behavior of authentication.getName() to return the expected username
        when(authentication.getName()).thenReturn("username");

        // Mock the behavior of authorRepository.existsAuthorByIdAndEmail() to return true
        when(authorRepository.existsAuthorByIdAndEmail(anyInt(), eq("username"))).thenReturn(true);

        // Create an Author object with ID 1
        Author author = new Author();
        author.setId(1); // Set the ID to 1

        // Mock the behavior of authorRepository to return the Author object when findById is called with ID 1
        when(authorRepository.findById(eq(1))).thenReturn(Optional.of(author));

        // Verify that the mocked Author object is not null
        assertNotNull(authorRepository.findById(1).orElse(null));

        // Mock the behavior of sentimentService to return a valid DetectSentimentResponse
        when(sentimentService.detectSentimentResponse(anyString())).thenReturn(DetectSentimentResponse.builder()
                .sentimentScore(SentimentScore.builder().positive(0.6f).negative(0.2f).neutral(0.1f).mixed(0.1f).build())
                .build());

        // Create a JournalEntry object
        JournalEntry journalEntryRequest = new JournalEntry();
        journalEntryRequest.setContent("Test content");
        journalEntryRequest.setCreationDate(LocalDate.now());
        journalEntryRequest.setAuthor(author); // Set the Author object with ID 1

        // Log statements for debugging
        System.out.println("Mocked Author object: " + authorRepository.findById(1).orElse(null));
        System.out.println("JournalEntry object before creation: " + journalEntryRequest);
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(new JournalEntry());
        // Invoke the method under test
        JournalEntry createdJournalEntry = journalEntryService.createJournalEntry(1, journalEntryRequest);

        // Log the createdJournalEntry for debugging
        System.out.println("Created JournalEntry: " + createdJournalEntry);

        // Verify that the returned JournalEntry is not null
        assertNotNull(createdJournalEntry);

        // Verify that the journalEntryRepository.save method was called once
        verify(journalEntryRepository, times(1)).save(any(JournalEntry.class));
    }


}

