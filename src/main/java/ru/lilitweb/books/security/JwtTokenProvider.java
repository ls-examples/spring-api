package ru.lilitweb.books.security;

import org.springframework.security.core.Authentication;
import ru.lilitweb.books.domain.Authority;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface JwtTokenProvider {
    String createToken(String username, Set<Authority> roles);
    Authentication getAuthentication(String token);
    String getUsername(String token);
    String resolveToken(HttpServletRequest req);
    boolean validateToken(String token);
}
