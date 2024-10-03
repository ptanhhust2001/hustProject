package com.document.Documentweb.service.user;

import com.document.Documentweb.constrant.enums.Role;
import com.document.Documentweb.dto.User.UserReqDTO;
import com.document.Documentweb.dto.User.UserResDTO;
import com.document.Documentweb.entity.User;
import com.document.Documentweb.exception.AppException;
import com.document.Documentweb.exception.ErrorCode;
import com.document.Documentweb.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServicesImpl {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserResDTO createUser(UserReqDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = mapper.map(dto,User.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        user = userRepository.save(user);
        return mapper.map(user, UserResDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResDTO> getUsers() {
        return userRepository.findAll().stream().map(user -> mapper.map(user, UserResDTO.class)).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
    public UserResDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return mapper.map(user, UserResDTO.class);
    }

    public UserResDTO getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return mapper.map(user, UserResDTO.class);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
