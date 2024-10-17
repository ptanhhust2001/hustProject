package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.User.UserReqDTO;
import com.document.Documentweb.dto.User.UserResDTO;
import com.document.Documentweb.dto.User.UserUpdateDTO;
import com.document.Documentweb.service.user.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/users")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    IUserService services;
    @NonFinal
    @Value("${avatar.upload-dir}")
    String uploadDir;

    @GetMapping
    public ResponseDTO<List<UserResDTO>> getAllUsers() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName() + " " + authentication.getAuthorities().stream().toString());
        return ResponseDTO.success(services.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseDTO<UserResDTO> getUserById(@PathVariable Long id) {
        return ResponseDTO.success(services.getUserById(id));
    }

    @GetMapping("/current")
    public ResponseDTO<UserResDTO> getCurrentUser() {
        return ResponseDTO.success(services.getCurrentUser());
    }

    @GetMapping("/avatar")
    public ResponseEntity<Resource> viewAvatar(@RequestParam Long id) {
        String fileName = services.getAvatarUrl(id);
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)  // Hoặc MediaType.IMAGE_PNG nếu là ảnh PNG
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseDTO<UserResDTO> createUser(@RequestBody @Valid UserReqDTO user) {
        return ResponseDTO.success(services.createUser(user));
    }

    @PostMapping(value = "/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam Long id) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            // Lưu ảnh vào thư mục
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath);
            String pathAvatar = uploadDir + fileName;
            services.uploadAvatar(id, pathAvatar);
            String fileUrl = "/api/avatars/view/" + fileName;

            return ResponseEntity.ok().body(ResponseDTO.success("File uploaded successfully"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDTO.fail("Failed to upload file"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteUser(@PathVariable Long id) {
        services.deleteUser(id);
        return ResponseDTO.success();
    }

    @PutMapping("/{id}")
    public ResponseDTO<UserResDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {
        return ResponseDTO.success(services.updateUser(id, dto));
    }

}
