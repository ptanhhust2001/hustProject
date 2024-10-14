package com.document.Documentweb.service.subject;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.subject.SubjectReqDTO;
import com.document.Documentweb.dto.subject.SubjectResDTO;
import com.document.Documentweb.entity.Subject;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.SubjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements ISubjectService{
    SubjectRepository repository;
    ModelMapper mapper;


    //FAILED

    public List<SubjectResDTO> findAll() {
        return repository.findAll().stream().map(subject -> mapper.map(subject, SubjectResDTO.class)).toList();
    }

    public SubjectResDTO findById(Long id) {
        Subject data = repository.findById(id)
                .orElseThrow(()
                        -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.SUBJECT_NOT_FOUND, List.of(id))));
        return mapper.map(data, SubjectResDTO.class);
    }

    public SubjectResDTO create(SubjectReqDTO dto) {
        Subject data = mapper.map(dto, Subject.class);
        if (repository.findByName(dto.getName()).isEmpty()) throw new BookException(FunctionError.CREATE_FAL, ErrorCommon.SUBJECT_EXISTED);
        repository.save(data);
        return mapper.map(repository.save(data), SubjectResDTO.class);
    }

    public SubjectResDTO update(Long id, SubjectReqDTO dto) {
        Subject data = repository.findById(id)
                .orElseThrow(()
                        -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.SUBJECT_NOT_FOUND, List.of(id))));
        data =  mapper.map(dto, Subject.class);
        data.setId(id);
        repository.save(data);
        return mapper.map(data, SubjectResDTO.class);
    }
}