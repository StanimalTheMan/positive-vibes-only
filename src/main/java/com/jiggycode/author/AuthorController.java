package com.jiggycode.author;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController {
    private final authorService authorService;

    public authorController(authorService authorService) {
        this.authorService = authorService;
    }

    /*
    @RequestMapping(
            path = "api/v1/author",
            method = RequestMethod.GET
    )*/
    @GetMapping("api/v1/authors")
    public List<Author> getauthors() {
        return authorService.getAllauthors();
    }

    @GetMapping("api/v1/authors/{authorId}")
    public author getauthor(
            @PathVariable("authorId") Integer authorId) {
        return authorService.getauthor(authorId);
    }
}
