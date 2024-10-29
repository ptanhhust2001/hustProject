package com.document.Documentweb.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "question")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String question;
    String firstAnswer;
    String secondAnswer;
    String thirdAnswer;
    String fourthAnswer;
    String correctAnswer;


    @ManyToOne
    @JoinColumn(name = "exam_id")
    Exam exam;

    public Question(String question, String firstAnswer, String secondAnswer, String thirdAnswer, String fourthAnswer, String correctAnswer, Exam exam) {
        this.question = question;
        this.firstAnswer = firstAnswer;
        this.secondAnswer = secondAnswer;
        this.thirdAnswer = thirdAnswer;
        this.fourthAnswer = fourthAnswer;
        this.correctAnswer = correctAnswer;
        this.exam = exam;
    }
}
