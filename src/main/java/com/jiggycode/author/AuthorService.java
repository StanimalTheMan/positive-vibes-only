package com.jiggycode.author;

import com.jiggycode.exception.DuplicateResourceException;
import com.jiggycode.exception.RequestValidationException;
import com.jiggycode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorDao authorDao;
    private final AuthorDTOMapper authorDTOMapper;

    private final PasswordEncoder passwordEncoder;

    public AuthorService(@Qualifier("jdbc") AuthorDao authorDao,
                         AuthorDTOMapper authorDTOMapper, PasswordEncoder passwordEncoder) {
        this.authorDao = authorDao;
        this.authorDTOMapper = authorDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorDao.selectAllAuthors()
                .stream()
                .map(author -> new AuthorDTO(
                        author.getId(),
                        author.getName(),
                        author.getEmail(),
                        author.getAge(),
                        author.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()),
                        author.getUsername()
                ))
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthor(Integer id) {
        return authorDao.selectAuthorById(id)
                .map(authorDTOMapper)
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
                passwordEncoder.encode(authorRegistrationRequest.password()),
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

    public void updateAuthor(Integer authorId,
                             AuthorUpdateRequest updateRequest) {

        Author author = authorDao.selectAuthorById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "author with id [%s] not found".formatted(authorId)
                ));


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
