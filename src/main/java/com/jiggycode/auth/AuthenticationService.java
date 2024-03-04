package com.jiggycode.auth;

import com.jiggycode.author.Author;
import com.jiggycode.author.AuthorDTO;
import com.jiggycode.author.AuthorDTOMapper;
import com.jiggycode.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AuthorDTOMapper authorDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, AuthorDTOMapper authorDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authorDTOMapper = authorDTOMapper;
        this.jwtUtil = jwtUtil;
    }
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Author principal = (Author) authentication.getPrincipal();
        AuthorDTO authorDTO = authorDTOMapper.apply(principal);
        String token = jwtUtil.issueToken(authorDTO.username(), authorDTO.roles());
        return new AuthenticationResponse(token, authorDTO);
    }

}
