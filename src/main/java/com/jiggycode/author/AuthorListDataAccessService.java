package com.jiggycode.author;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class AuthorListDataAccessService implements AuthorDao {

    // in memory db for now
    private static final List<Author> authors;

    static {
        authors = new ArrayList<>();

        Author stanimal = new Author(
                1,
                "Stanimal",
                "stanimal@gmail.com",
                "password",
                26
        );
        authors.add(stanimal);

        Author jiggy = new Author(
                2,
                "Jiggy",
                "jiggy@gmail.com",
                "password",
                25
        );
        authors.add(jiggy);
    }
    @Override
    public List<Author> selectAllAuthors() {
        return authors;
    }

    @Override
    public Optional<Author> selectAuthorById(Integer id) {
        return authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertAuthor(Author author) {
        authors.add(author);
    }

    @Override
    public boolean existsAuthorWithEmail(String email) {
        return authors.stream()
                .anyMatch(a -> a.getEmail().equals(email));
    }

    @Override
    public boolean existsAuthorWithId(Integer id) {
        return authors.stream()
                .anyMatch(a -> a.getId().equals(id));
    }

    @Override
    public void deleteAuthorById(Integer authorId) {
        authors.stream()
                .filter(a -> a.getId().equals(authorId))
                .findFirst()
                .ifPresent(authors::remove);
    }

    @Override
    public void updateAuthor(Author author) {
        authors.add(author);
    }

    @Override
    public Optional<Author> selectAuthorByEmail(String email) {
        return authors.stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();
    }

}
