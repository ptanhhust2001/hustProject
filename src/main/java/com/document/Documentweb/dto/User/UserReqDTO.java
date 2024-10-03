package com.document.Documentweb.dto.User;

import com.document.Documentweb.constrant.EError;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static com.document.Documentweb.constrant.EError.FORMAT_INVALID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReqDTO {
    private String username;
    @Size(min = 6, max = 20, message = FORMAT_INVALID)
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate dob;
}
