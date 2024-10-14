package com.document.Documentweb.dto.subject;

import com.document.Documentweb.entity.Post;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectResDTO {
    Long id;

    String name;

    List<Post> posts;
}
