package com.document.Documentweb.entity;

import com.document.Documentweb.constrant.EError;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Pattern(regexp = "^[A-Z0-9_]", message = EError.FORMAT_INVALID)
    private String name;
    private String description;

    @ManyToMany
    Set<Permission> permissions;

}
