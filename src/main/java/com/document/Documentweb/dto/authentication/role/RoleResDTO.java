package com.document.Documentweb.dto.authentication.role;

import com.document.Documentweb.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResDTO {
    private String name;
    private String description;
    Set<Permission> permissions;
}
