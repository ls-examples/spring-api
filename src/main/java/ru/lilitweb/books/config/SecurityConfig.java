package ru.lilitweb.books.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.lilitweb.books.repository.UserRepository;
import ru.lilitweb.books.security.JwtConfigurer;
import ru.lilitweb.books.security.JwtTokenProvider;
import ru.lilitweb.books.security.RestAuthenticationEntryPoint;
import ru.lilitweb.books.security.UserDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/book").hasRole("ADMIN")
                .regexMatchers(HttpMethod.PUT, "/api/v1/book/[A-Za-z0-9]+").hasRole("ADMIN")
                .regexMatchers(HttpMethod.DELETE, "/api/v1/book/[A-Za-z0-9]+").hasRole("ADMIN")
                .regexMatchers(HttpMethod.GET, "/api/v1/book/[A-Za-z0-9]+").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                .apply(new JwtConfigurer(new JwtTokenProvider()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService mongoUserDetails(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Autowired
    public void configAuthBuilder(AuthenticationManagerBuilder builder,
                                  @Qualifier("mongoUserDetails") UserDetailsService userDetailsService) throws Exception {
        builder.userDetailsService(userDetailsService);
    }
}
