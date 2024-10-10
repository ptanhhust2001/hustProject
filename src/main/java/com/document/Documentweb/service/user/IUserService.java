package com.document.Documentweb.service.user;

import com.document.Documentweb.dto.User.UserReqDTO;
import com.document.Documentweb.dto.User.UserResDTO;
import com.document.Documentweb.dto.User.UserUpdateDTO;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface IUserService {
    UserResDTO createUser(UserReqDTO dto);

    //    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('READ_DATA')")
    List<UserResDTO> getUsers();

    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
    UserResDTO getUserById(Long id);

    UserResDTO getCurrentUser();

    void deleteUser(Long userId);

    UserResDTO updateUser(long id, UserUpdateDTO dto);
}
