package com.document.Documentweb.service.user;

import com.document.Documentweb.constrant.CommonConstrant;
import com.document.Documentweb.constrant.enums.Role;
import com.document.Documentweb.dto.User.UserReqDTO;
import com.document.Documentweb.dto.User.UserResDTO;
import com.document.Documentweb.dto.User.UserUpdateDTO;
import com.document.Documentweb.entity.User;
import com.document.Documentweb.exception.AppException;
import com.document.Documentweb.exception.ErrorCode;
import com.document.Documentweb.repository.UserRepository;
import com.document.Documentweb.service.RoleServiceImpl;
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
public class UserServicesImpl implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper userMapper;
    private final RoleServiceImpl roleService;

    @Override
    public UserResDTO createUser(UserReqDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.map(dto,User.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//`        user.setRoles(roles);`

        user = userRepository.save(user);
        return userMapper.map(user, UserResDTO.class);
    }

    @PreAuthorize("hasAuthority('READ_DATA')")
    @Override
    public List<UserResDTO> getUsers() {
        return userRepository.findAll().stream().map(user -> userMapper.map(user, UserResDTO.class)).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
    @Override
    public UserResDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.map(user, UserResDTO.class);
    }

    @Override
    public UserResDTO getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.map(user, UserResDTO.class);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserResDTO updateUser(long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.map(dto, user);
        user.setId(id);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return userMapper.map(user, UserResDTO.class);
    }

    @Override
    public void uploadAvatar(Long id, String url) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setAvatarUrl(url);
        userRepository.save(user);
    }

    @Override
    public String getAvatarUrl(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user.getAvatarUrl() == null ? CommonConstrant.DEFAULT_AVATAR : user.getAvatarUrl();
    }
}
