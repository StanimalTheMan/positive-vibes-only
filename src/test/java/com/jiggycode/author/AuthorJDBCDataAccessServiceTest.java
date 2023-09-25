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
    void willReturnEmptyWhenSelectAuthorById() {
        // Given
        int id = 0;

        // When
        var actual = underTest.selectAuthorById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void existsAuthorWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Author author = new Author(
                name,
                email,
                20
        );

        underTest.insertAuthor(author);

        // When
        boolean actual = underTest.existsAuthorWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsAuthorWithEmailReturnsFalseWhenDoesNotExist() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsAuthorWithEmail(email);

        // Then
        assertThat(actual).isFalse();
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