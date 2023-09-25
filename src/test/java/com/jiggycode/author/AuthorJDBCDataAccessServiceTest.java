package com.jiggycode.author;

import com.jiggycode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
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
        List<Author> actual = underTest.selectAllAuthors();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectAuthorById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Author author = new Author(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertAuthor(author);

        int id = underTest.selectAllAuthors()
                .stream()
                .filter(a -> a.getEmail().equals(email))
                .map(Author::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Author> actual = underTest.selectAuthorById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(a -> {
            assertThat(a.getId()).isEqualTo(id);
            assertThat(a.getName()).isEqualTo(author.getName());
            assertThat(a.getEmail()).isEqualTo(author.getEmail());
            assertThat(a.getAge()).isEqualTo(author.getAge());
        });
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