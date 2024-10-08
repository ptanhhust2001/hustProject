package com.document.Documentweb.dto.authentication.role;

import com.document.Documentweb.constrant.EError;
import com.document.Documentweb.entity.Permission;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleReqDTO {
    @Pattern(regexp = "^[A-Z0-9_]*$", message = EError.FORMAT_INVALID)
    private String name;
    private String description;

    List<String> permissions;
}
