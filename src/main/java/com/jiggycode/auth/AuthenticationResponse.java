package com.jiggycode.auth;

import com.jiggycode.author.AuthorDTO;

public record AuthenticationResponse(
        String token,
        AuthorDTO authorDTO
) {
}
