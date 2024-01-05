package com.jiggycode.author;

import com.jiggycode.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final JWTUtil jwtUtil;

    public AuthorController(AuthorService authorService,
                            JWTUtil jwtUtil) {
        this.authorService = authorService;
        this.jwtUtil = jwtUtil;
    }

    /*
    @RequestMapping(
            path = "api/v1/author",
            method = RequestMethod.GET
    )*/
    @GetMapping
    public List<AuthorDTO> getAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("{authorId}")
    public AuthorDTO getAuthor(
            @PathVariable("authorId") Integer authorId) {
        return authorService.getAuthor(authorId);
    }

    @PostMapping
    public ResponseEntity<?> registerAuthor(
            @RequestBody AuthorRegistrationRequest request) {
        authorService.addAuthor(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("{authorId}")
    public void deleteAuthor(
            @PathVariable("authorId") Integer authorId) {
        authorService.deleteAuthorById(authorId);
    }

    @PutMapping("{authorId}")
    public void updateAuthor(
            @PathVariable("authorId") Integer authorId,
            @RequestBody AuthorUpdateRequest updateRequest) {
        authorService.updateAuthor(authorId, updateRequest);
    }
}
