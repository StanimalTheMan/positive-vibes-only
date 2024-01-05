package com.jiggycode.author;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthorDTOMapper implements Function<Author, AuthorDTO> {
    @Override
    public AuthorDTO apply(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getEmail(),
                author.getAge(),
                author.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                author.getUsername());
    }
}
