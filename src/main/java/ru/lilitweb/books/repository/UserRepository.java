package ru.lilitweb.books.repository;

import org.springframework.data.repository.CrudRepository;
import ru.lilitweb.books.domain.User;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email);
}
