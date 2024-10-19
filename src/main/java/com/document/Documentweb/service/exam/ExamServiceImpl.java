package com.document.Documentweb.service.exam;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.exam.ExamReqDTO;
import com.document.Documentweb.dto.exam.ExamResDTO;
import com.document.Documentweb.dto.exam.ExamUpdateDTO;
import com.document.Documentweb.entity.ClassEntity;
import com.document.Documentweb.entity.Exam;
import com.document.Documentweb.entity.Subject;
import com.document.Documentweb.entity.User;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.ClassEntityRepository;
import com.document.Documentweb.repository.ExamRepository;
import com.document.Documentweb.repository.SubjectRepository;
import com.document.Documentweb.repository.UserRepository;
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
public class ExamServiceImpl implements IExamService{
    ExamRepository repository;
    ModelMapper examMapper;
    ClassEntityRepository classEntityRepository;
    UserRepository userRepository;
    SubjectRepository subjectRepository;

    @Override
    public List<ExamResDTO> findAll(String advanceSearch) {
        List<Exam> datas = repository.findAll(BaseSpecs.searchQuery(advanceSearch));
        return Utils.mapList(examMapper, datas, ExamResDTO.class);
    }

    @Override
    public ExamResDTO findById(Long id) {
        Exam data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.EXAM_NOT_FOUND, List.of(id))));
        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public ExamResDTO create(ExamReqDTO dto) {
        Exam data = examMapper.map(dto, Exam.class);
        Map<Object, Object> errorMap = new HashMap<>();
        Optional<ClassEntity> classOpt = classEntityRepository.findById(dto.getClassEntityId());
        Optional<Subject> subjectOpt = subjectRepository.findById(dto.getSubjectId());
        Optional<User> userOpt = userRepository.findById(dto.getUserId());

        if (classOpt.isEmpty()) errorMap.put(ErrorCommon.CLASS_DOES_NOT_EXIST, List.of(dto.getClassEntityId()));
        if (subjectOpt.isEmpty()) errorMap.put(ErrorCommon.SUBJECT_DOES_NOT_EXIST, List.of(dto.getSubjectId()));
        if (userOpt.isEmpty()) errorMap.put(ErrorCommon.USER_DOES_NOT_EXIST, List.of(dto.getUserId()));

        if (!errorMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, errorMap);

        data.setClassEntity(classOpt.get()  );
        data.setSubject(subjectOpt.get());
        data.setUser(userOpt.get());

        // thiếu

        repository.save(data);

        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public ExamResDTO update(Long id, ExamUpdateDTO dto) {
        Exam data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.EXAM_NOT_FOUND, List.of(id))));
        examMapper.map(dto, data);

        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        List<Long> notFound = ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if (!notFound.isEmpty()) throw new BookException(FunctionError.DELETE_FAILSE, Map.of(ErrorCommon.EXAM_NOT_FOUND, notFound));
        repository.deleteAllById(ids);
    }

}
