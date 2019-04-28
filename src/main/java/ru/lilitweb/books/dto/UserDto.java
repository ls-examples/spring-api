package ru.lilitweb.books.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String fullname;

    private String password;

    private String passwordConfirmation;

    private String email;
}
