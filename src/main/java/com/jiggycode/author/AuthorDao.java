package com.jiggycode.author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    List<Author> selectAllAuthors();
    Optional<Author> selectAuthorById(Integer id);
    void insertAuthor(Author author);
    boolean existsAuthorWithEmail(String email);
}
