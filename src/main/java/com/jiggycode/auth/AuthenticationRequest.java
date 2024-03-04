package com.jiggycode.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
