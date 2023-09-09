package com.jiggycode.author;

public record AuthorUpdateRequest (
        String name,
        String email,
        Integer age
) {

}
