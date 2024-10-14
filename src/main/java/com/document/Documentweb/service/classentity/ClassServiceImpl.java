package com.document.Documentweb.service.classentity;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.classenity.ClassReqDTO;
import com.document.Documentweb.dto.classenity.ClassResDTO;
import com.document.Documentweb.entity.ClassEntity;
import com.document.Documentweb.entity.Subject;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.ClassEntityRepository;
import com.document.Documentweb.repository.SubjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassServiceImpl implements IClassService{
    ClassEntityRepository repository;
    SubjectRepository subjectRepository;
    ModelMapper mapper;

    @Override
    public ClassResDTO create(ClassReqDTO dto) {
        ClassEntity data = mapper.map(dto, ClassEntity.class);
        Map<Object, Object> errorMap = new HashMap<>();

        List<Subject> subjects = getAllSubjectByName(dto.getSubjects(), errorMap);
        
        if (repository.findByName(dto.getName()).isEmpty()) errorMap.put(ErrorCommon.CLASS_ALREADY_EXISTS, List.of(data.getName()));
        if (!errorMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAL,errorMap);
        data.setSubjects(subjects);
        repository.save(data);

        return mapper.map(data, ClassResDTO.class);
    }

    @Override
    public List<ClassResDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(data -> mapper.map(data, ClassResDTO.class))
                .toList();
    }

    @Override
    public ClassResDTO findById(Long id) {
        Optional<ClassEntity> data = repository.findById(id);
        if (data.isEmpty()) throw new BookException(FunctionError.NOT_FOUND, List.of(id));
        return mapper.map(data.get(), ClassResDTO.class);
    }

    @Override
    public ClassResDTO update(Long id, ClassReqDTO dto) {
        Optional<ClassEntity> data = repository.findById(id);
        if (data.isEmpty()) throw new BookException(FunctionError.NOT_FOUND, List.of(id));

        ClassEntity updateData = mapper.map(dto, ClassEntity.class);
        updateData.setId(id);
        Map<Object, Object> errorMap = new HashMap<>();

        List<Subject> subjects = getAllSubjectByName(dto.getSubjects(), errorMap);
        if (!errorMap.isEmpty()) throw new BookException(FunctionError.UPDATE_FAL, errorMap);
        updateData.setSubjects(subjects);
        repository.save(updateData);
        return mapper.map(updateData, ClassResDTO.class);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        Map<Object, Object> errorMap = new HashMap<>();
        List<Long> notFound = ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if (!notFound.isEmpty()) errorMap.put(ErrorCommon.CLASS_NOT_FOUND, notFound);
        if (!errorMap.isEmpty()) throw new BookException(FunctionError.DELETE_FAL, errorMap);

        repository.deleteAllById(ids);
    }

    private List<Subject> getAllSubjectByName(List<String> names,  Map<Object, Object> errorMap) {
        List<Subject> subjects = new ArrayList<>();
        for (String str : names) {
            Optional<Subject> subject = subjectRepository.findByName(str);
            if (subject.isEmpty()) errorMap.computeIfAbsent(ErrorCommon.SUBJECT_NOT_FOUND, k -> new ArrayList<>().add(str));
            else subjects.add(subject.get());
        }
        return subjects;
    }

}
