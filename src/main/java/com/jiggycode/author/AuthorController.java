package com.jiggycode.author;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /*
    @RequestMapping(
            path = "api/v1/author",
            method = RequestMethod.GET
    )*/
    @GetMapping
    public List<Author> getAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("{authorId}")
    public Author getAuthor(
            @PathVariable("authorId") Integer authorId) {
        return authorService.getAuthor(authorId);
    }

    @PostMapping
    public void registerAuthor(
            @RequestBody AuthorRegistrationRequest request) {
        authorService.addAuthor(request);
    }

    @DeleteMapping("{authorId}")
    public void deleteAuthor(
            @PathVariable("authorId") Integer authorId) {
        authorService.deleteAuthorById(authorId);
    }
}
