package com.document.Documentweb.dto.exam;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResDTO {
    Long id;
    String name;
    Long classEntityId;
    Long subjectId;
    Long userId;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String createBy;
    String updateBy;
}
