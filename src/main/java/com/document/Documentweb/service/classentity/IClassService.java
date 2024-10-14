package com.document.Documentweb.service.classentity;


import com.document.Documentweb.dto.classenity.ClassReqDTO;
import com.document.Documentweb.dto.classenity.ClassResDTO;

import java.util.List;

public interface IClassService {
    ClassResDTO create(ClassReqDTO dto);

    List<ClassResDTO> findAll();

    ClassResDTO findById(Long id);

    ClassResDTO update(Long id, ClassReqDTO dto);

    void deleteAllByIds(List<Long> ids);
}
