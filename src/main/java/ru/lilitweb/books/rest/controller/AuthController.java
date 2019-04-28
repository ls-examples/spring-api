package ru.lilitweb.books.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.lilitweb.books.dto.LoginDto;
import ru.lilitweb.books.repository.UserRepository;
import ru.lilitweb.books.security.JwtTokenProviderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private JwtTokenProviderImpl jwtTokenProviderImpl;

    private UserRepository users;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProviderImpl jwtTokenProviderImpl, UserRepository users) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProviderImpl = jwtTokenProviderImpl;
        this.users = users;
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody LoginDto data) {
        try {
            String username = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProviderImpl.createToken(
                    username,
                    this.users.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getAuthorities()
            );
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}
