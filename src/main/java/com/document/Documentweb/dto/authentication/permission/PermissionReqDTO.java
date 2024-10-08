package com.document.Documentweb.dto.authentication.permission;

import com.document.Documentweb.constrant.EError;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionReqDTO {
    @Pattern(regexp = "^[A-Z0-9_]*$", message = EError.FORMAT_INVALID)
    private String name;
    private String description;
}
