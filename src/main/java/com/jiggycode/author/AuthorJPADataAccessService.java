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
        return authorRepository.findById(id);
    }

    @Override
    public void insertAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public boolean existsAuthorWithEmail(String email) {
        return authorRepository.existsAuthorByEmail(email);
    }

    @Override
    public boolean existsAuthorWithId(Integer id) {
        return authorRepository.existsAuthorById(id);
    }

    @Override
    public void deleteAuthorById(Integer authorId) {
        authorRepository.deleteById(authorId);
    }

    @Override
    public void updateAuthor(Author update) {
        authorRepository.save(update);
    }

}
