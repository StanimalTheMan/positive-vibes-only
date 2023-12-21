package com.jiggycode.author;

public record AuthorRegistrationRequest (
        String name,
        String email,
        String password,
        Integer age
) {

}
