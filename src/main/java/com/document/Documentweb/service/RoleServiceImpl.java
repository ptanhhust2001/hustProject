package com.document.Documentweb.service;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.authentication.role.RoleReqDTO;
import com.document.Documentweb.dto.authentication.role.RoleResDTO;
import com.document.Documentweb.entity.Permission;
import com.document.Documentweb.entity.Role;
import com.document.Documentweb.exception.AppException;
import com.document.Documentweb.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.document.Documentweb.exception.ErrorCode.ROLE_NOT_EXISTED;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl {
    RoleRepository repo;
    ModelMapper mapper;
    PermissionServiceImpl permissionService;

    public ResponseDTO<RoleResDTO> create(RoleReqDTO dto) {
        Role data = mapper.map(dto, Role.class);
        Set<Permission> permissions =new HashSet<>(permissionService.getAllByIds(dto.getPermissions()));
        data.setPermissions(permissions);

        repo.save(data);

        return ResponseDTO.success(mapper.map(data, RoleResDTO.class));
    }

    public ResponseDTO<List<RoleResDTO>> getAll() {
        return ResponseDTO.success(repo.findAll().stream().map(data ->  mapper.map(data, RoleResDTO.class)).toList());
    }

    public void delete(String name) {
        Role data = repo.findById(name).orElseThrow(() -> new AppException(ROLE_NOT_EXISTED));
        repo.deleteById(name);
    }

    public List<Role> getAllById(List<String> ids) {
        return repo.findAllById(ids);
    }
}