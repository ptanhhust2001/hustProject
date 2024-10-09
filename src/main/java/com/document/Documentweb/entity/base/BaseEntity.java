package com.document.Documentweb.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class BaseEntity {
    private String createBy;
    private String updateBy;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
