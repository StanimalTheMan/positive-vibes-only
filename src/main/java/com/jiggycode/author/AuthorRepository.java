package com.jiggycode.author;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    boolean existsAuthorByEmail(String email);
    boolean existsAuthorById(Integer id);
}
