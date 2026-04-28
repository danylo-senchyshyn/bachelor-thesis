package com.example.monolith.user.service;

import com.example.monolith.user.controller.dto.CreateUserRequest;
import com.example.monolith.user.controller.dto.UserResponse;
import com.example.monolith.user.entity.User;
import com.example.monolith.user.reposirtory.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                    "User with email already exists: " + request.email()
            );
        }

        User user = User.builder()
                .name(request.fullName())
                .email(request.email())
                .build();

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> { return toResponse(user);})
                .orElseThrow(() -> { throw  new EntityNotFoundException("User not found: " + id);});
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();

        return users;
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}
