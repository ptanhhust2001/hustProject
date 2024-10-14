package com.document.Documentweb.dto.classenity;

import com.document.Documentweb.entity.Post;
import com.document.Documentweb.entity.Subject;
import java.util.List;
import java.util.Set;

public class ClassResDTO {

    Long id;

    String name;

    List<Subject> subjects;

    Set<Post> posts;

}
