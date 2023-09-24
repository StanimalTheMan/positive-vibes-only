package com.jiggycode.author;

import com.jiggycode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        Author author = new Author(
            FAKER.name().fullName(),
            FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
            20
        );
        underTest.insertAuthor(author);

        // When
        List<Author> authors = underTest.selectAllAuthors();

        // Then
        assertThat(authors).isNotEmpty();
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