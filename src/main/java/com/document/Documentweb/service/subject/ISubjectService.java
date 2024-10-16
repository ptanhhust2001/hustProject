package com.document.Documentweb.service.subject;

import com.document.Documentweb.dto.subject.SubjectReqDTO;
import com.document.Documentweb.dto.subject.SubjectResDTO;

import java.util.List;

public interface ISubjectService {
    //FAILED
    List<SubjectResDTO> findAll();

    SubjectResDTO findById(Long id);

    SubjectResDTO create(SubjectReqDTO dto);

    SubjectResDTO update(Long id, SubjectReqDTO dto);
}
