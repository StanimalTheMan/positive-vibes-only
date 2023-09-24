package com.jiggycode.author;

import com.jiggycode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class AuthorJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private AuthorJDBCDataAccessService underTest;
    private AuthorRowMapper authorRowMapper = new AuthorRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new AuthorJDBCDataAccessService(
                getJdbcTemplate(),
                authorRowMapper
        );
    }

    @Test
    void selectAllAuthors() {
        // Given

        // When

        // Then
    }

    @Test
    void selectAuthorById() {
        // Given

        // When

        // Then
    }

    @Test
    void insertAuthor() {
        // Given

        // When

        // Then
    }

    @Test
    void existsAuthorWithEmail() {
        // Given

        // When

        // Then
    }

    @Test
    void existsAuthorWithId() {
        // Given

        // When

        // Then
    }

    @Test
    void deleteAuthorById() {
        // Given

        // When

        // Then
    }

    @Test
    void updateAuthor() {
        // Given

        // When

        // Then
    }
}