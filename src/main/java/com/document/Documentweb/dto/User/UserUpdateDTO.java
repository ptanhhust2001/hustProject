package com.document.Documentweb.dto.User;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import static com.document.Documentweb.constrant.EError.FORMAT_INVALID;

@Data
public class UserUpdateDTO {
    private String username;
    @Size(min = 6, max = 20, message = FORMAT_INVALID)
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private List<String> roles;
}
