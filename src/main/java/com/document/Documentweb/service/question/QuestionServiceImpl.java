package com.document.Documentweb.service.question;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.question.QuestionReqDTO;
import com.document.Documentweb.dto.question.QuestionResDTO;
import com.document.Documentweb.dto.question.QuestionUpdateDTO;
import com.document.Documentweb.entity.Exam;
import com.document.Documentweb.entity.Question;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.ExamRepository;
import com.document.Documentweb.repository.QuestionRepository;
import com.document.Documentweb.utils.spec.BaseSpecs;
import com.document.Documentweb.utils.spec.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements IQuestionService{
    QuestionRepository repository;
    ModelMapper questionMapper;
    ExamRepository examRepository;

    @Override
    public List<QuestionResDTO> findAll(String advanceSearch) {
        List<Question> datas = repository.findAll(BaseSpecs.searchQuery(advanceSearch));
        return Utils.mapList(questionMapper, datas, QuestionResDTO.class);
    }

    @Override
    public QuestionResDTO findById(Long id) {
        Question data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.QUESTION_NOT_FOUND, List.of(id))));
        return questionMapper.map(data, QuestionResDTO.class);
    }

    @Override
    public QuestionResDTO create(QuestionReqDTO dto) {
        Question data = questionMapper.map(dto, Question.class);
        Map<Object, Object> errorMap = new HashMap<>();
        Optional<Exam> examOpt = examRepository.findById(dto.getExamId());
        if (examOpt.isEmpty()) errorMap.put(ErrorCommon.EXAM_DOES_NOT_EXIST, List.of(dto.getExamId()));
        data.setExam(examOpt.get());

        repository.save(data);

        return questionMapper.map(data, QuestionResDTO.class);
    }

    @Override
    public QuestionResDTO update(Long id, QuestionUpdateDTO dto) {
        Question data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.QUESTION_NOT_FOUND, List.of(id))));
        questionMapper.map(dto, data);
        return questionMapper.map(data, QuestionResDTO.class);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        List<Long> notFound =  ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if(!notFound.isEmpty()) throw new BookException(FunctionError.DELETE_FAILED, Map.of(ErrorCommon.QUESTION_NOT_FOUND, notFound));
        repository.deleteAllById(ids);
    }
}
