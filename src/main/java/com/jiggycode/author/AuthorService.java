package com.jiggycode.author;

import com.jiggycode.exception.DuplicateResourceException;
import com.jiggycode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorDao authorDao;

    public AuthorService(@Qualifier("jpa") AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public List<Author> getAllAuthors() {
        return authorDao.selectAllAuthors();
    }

    public Author getAuthor(Integer id) {
        return authorDao.selectAuthorById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "author with id [%s] not found".formatted(id)
                ));
    }

    public void addAuthor(
            AuthorRegistrationRequest authorRegistrationRequest) {
        // check if email exists
        String email = authorRegistrationRequest.email();
        if (authorDao.existsAuthorWithEmail(email)) {
            throw new DuplicateResourceException(
                "email already taken"
            );
        }
        // add
        Author author = new Author(
                authorRegistrationRequest.name(),
                authorRegistrationRequest.email(),
                authorRegistrationRequest.age()
        );
        authorDao.insertAuthor(author);
    }

    public void deleteAuthorById(Integer authorId) {
        if (!authorDao.existsAuthorWithId(authorId)) {
            throw new ResourceNotFoundException(
                    "author with id [%s] not found".formatted(authorId)
            );
        }

        authorDao.deleteAuthorById(authorId);
    }
}
