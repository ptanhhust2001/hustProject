package com.document.Documentweb.dto.authentication.refresh;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RefreshTokenReqDTO {
    String token;
}
