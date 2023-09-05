package com.jiggycode.author;

public record AuthorRegistrationRequest (
    String name,
    String email,
    Integer age
) {

}
