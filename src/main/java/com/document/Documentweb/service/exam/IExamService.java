package com.document.Documentweb.service.exam;

import com.document.Documentweb.dto.exam.ExamReqDTO;
import com.document.Documentweb.dto.exam.ExamResDTO;
import com.document.Documentweb.dto.exam.ExamUpdateDTO;

import java.util.List;

public interface IExamService {
    List<ExamResDTO> findAll(String advanceSearch);

    ExamResDTO findById(Long id);

    ExamResDTO create(ExamReqDTO dto);

    ExamResDTO update(Long id, ExamUpdateDTO dto);

    void deleteAllById(List<Long> ids);
}
