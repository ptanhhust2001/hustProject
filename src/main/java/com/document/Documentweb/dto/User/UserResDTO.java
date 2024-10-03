package com.document.Documentweb.dto.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserResDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    Set<String> roles;
}
