package com.document.Documentweb.service.exam;

import com.document.Documentweb.dto.exam.ExamReqDTO;
import com.document.Documentweb.dto.exam.ExamReqOpenAiDTO;
import com.document.Documentweb.dto.exam.ExamResDTO;
import com.document.Documentweb.dto.exam.ExamUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IExamService {
    List<ExamResDTO> findAll(String advanceSearch);

    ExamResDTO findById(Long id);

    ExamResDTO create(ExamReqDTO dto);

    ExamResDTO update(Long id, ExamUpdateDTO dto);

    void deleteAllById(List<Long> ids);

    void upload(MultipartFile file , Long classId, Long subjectId) throws IOException;

    ExamResDTO createQuestionByOpenAi(ExamReqOpenAiDTO dto) throws JsonProcessingException;
}
