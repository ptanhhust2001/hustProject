package com.document.Documentweb.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "InValidateToken")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InValidateToken {
    @Id
    private String id;
    Date expiryTime;

}
