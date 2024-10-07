package com.document.Documentweb.service;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.authentication.permission.PermissionReqDTO;
import com.document.Documentweb.dto.authentication.permission.PermissionResDTO;
import com.document.Documentweb.entity.Permission;
import com.document.Documentweb.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl {
    PermissionRepository repo;
    ModelMapper mapper;

    @Transactional
    public ResponseDTO<PermissionResDTO> create(PermissionReqDTO dto) {
        Permission data = mapper.map(dto, Permission.class);
        repo.save(data);
        return ResponseDTO.success(mapper.map(data, PermissionResDTO.class));
    }

    public ResponseDTO<List<PermissionResDTO>> getAll() {
        return ResponseDTO.success(repo.findAll().stream().map(data ->  mapper.map(data, PermissionResDTO.class)).toList());
    }

    public ResponseDTO<List<PermissionResDTO>> delete(List<String> names) {
        Map<Object, List<String>> ErrorsMap = new HashMap<>();
        List<Permission> datas = new ArrayList<>();
        names.forEach(name -> {
            Optional<Permission> data = repo.findById(name);
            if (data.isEmpty()) ErrorsMap.computeIfAbsent("Permission not found", k -> new ArrayList<>()).add(name);
            else datas.add(data.get());

        });
        if (!ErrorsMap.isEmpty()) throw new RuntimeException("DELETE_FALSE");
        repo.deleteAll(datas);
        return ResponseDTO.success(datas.stream().map(data -> mapper.map(data, PermissionResDTO.class)).toList());
    }

    public Optional<Permission> getByName(String name) {
        return repo.findById(name);
    }

    public List<Permission> getAllByIds(List<String> names) {
        return repo.findAllById(names);
    }

}
