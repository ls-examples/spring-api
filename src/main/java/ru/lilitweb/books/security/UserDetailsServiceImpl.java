package ru.lilitweb.books.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lilitweb.books.model.UserDetailsImpl;
import ru.lilitweb.books.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + email + " not found"));
    }
}
