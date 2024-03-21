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
}

