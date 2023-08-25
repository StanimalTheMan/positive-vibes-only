package com.jiggycode.author;

import com.jiggycode.exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorDao authorDao;

    public AuthorService(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public List<Author> getAllAuthors() {
        return authorDao.selectAllAuthors();
    }

    public Author getAuthor(Integer id) {
        return authorDao.selectAuthorById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "user with id [%s] not found".formatted(id)
                ));
    }
}
