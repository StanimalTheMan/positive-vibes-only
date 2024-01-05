package com.jiggycode.author;

import java.util.List;

public record AuthorDTO(
        Integer id,
        String name,
        String email,
        Integer age,
        List<String> roles,
        String username
){

}
