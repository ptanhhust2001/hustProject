package com.document.Documentweb.service.question;

import com.document.Documentweb.dto.question.QuestionReqDTO;
import com.document.Documentweb.dto.question.QuestionResDTO;
import com.document.Documentweb.dto.question.QuestionUpdateDTO;

import java.util.List;

public interface IQuestionService {
    List<QuestionResDTO> findAll(String advanceSearch);

    QuestionResDTO findById(Long id);

    QuestionResDTO create(QuestionReqDTO dto);

    QuestionResDTO update(Long id, QuestionUpdateDTO dto);

    void deleteAllById(List<Long> ids);
}
