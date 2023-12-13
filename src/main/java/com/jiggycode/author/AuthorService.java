package com.jiggycode.author;

import com.jiggycode.exception.DuplicateResourceException;
import com.jiggycode.exception.RequestValidationException;
import com.jiggycode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorDao authorDao;

    public AuthorService(@Qualifier("jdbc") AuthorDao authorDao) {
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
                "password", authorRegistrationRequest.age()
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

    public void updateAuthor(Integer authorId,
                             AuthorUpdateRequest updateRequest) {

        Author author = getAuthor(authorId);

        boolean isChanged = false;

        if (updateRequest.name() != null && updateRequest.name().equals(author.getName())) {
            author.setName(updateRequest.name());
            isChanged = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(author.getAge())) {
            author.setAge(updateRequest.age());
            isChanged = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(author.getEmail())) {
            if (authorDao.existsAuthorWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            author.setEmail(updateRequest.email());
            isChanged = true;
        }

        if (!isChanged) {
            throw new RequestValidationException("no data changes found");
        }

        authorDao.updateAuthor(author);
    }
}
