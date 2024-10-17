package com.document.Documentweb.dto.classenity;

import com.document.Documentweb.entity.Post;
import com.document.Documentweb.entity.Subject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassResDTO {

    Long id;

    String name;
}
