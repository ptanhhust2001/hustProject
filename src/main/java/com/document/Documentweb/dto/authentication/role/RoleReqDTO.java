package com.document.Documentweb.dto.authentication.role;

import com.document.Documentweb.constrant.EError;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleReqDTO {
    @Pattern(regexp = "^[A-Z0-9_]*$", message = EError.FORMAT_INVALID)
    private String name;
    private String description;

    List<String> permissions;
}
