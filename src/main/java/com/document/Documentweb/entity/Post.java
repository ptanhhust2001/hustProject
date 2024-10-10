package com.document.Documentweb.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String content;
    String description;
    String author;

    @ManyToOne
    @JoinColumn(name = "class_id")
    ClassEntity aClass;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @OneToMany
    List<Comment> comments;

    @OneToMany
    List<Material> materials;
}
