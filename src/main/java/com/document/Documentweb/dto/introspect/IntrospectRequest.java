package com.document.Documentweb.dto.introspect;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class IntrospectRequest {
    String token;
}
