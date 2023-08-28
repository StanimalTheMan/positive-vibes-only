package com.jiggycode.author;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class AuthorJPADataAccessService implements AuthorDao {

    private final AuthorRepository authorRepository;

    public AuthorJPADataAccessService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> selectAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> selectAuthorById(Integer id) {
        return Optional.empty();
    }
}
