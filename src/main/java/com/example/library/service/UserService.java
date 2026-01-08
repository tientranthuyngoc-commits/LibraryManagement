package com.example.library.service;

import com.example.library.domain.Role;
import com.example.library.domain.User;
import com.example.library.domain.enumtype.UserStatus;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional
    public User createUser(User user, List<String> roleNames) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(resolveRoles(roleNames == null ? List.of() : roleNames));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User update, List<String> roleNames) {
        User user = findById(id);
        user.setFullName(update.getFullName());
        user.setEmail(update.getEmail());
        user.setPhone(update.getPhone());
        if (update.getPasswordHash() != null && !update.getPasswordHash().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(update.getPasswordHash()));
        }
        user.setRoles(resolveRoles(roleNames == null ? List.of() : roleNames));
        return userRepository.save(user);
    }

    @Transactional
    public void toggleStatus(Long id) {
        User user = findById(id);
        if (user.getStatus() == UserStatus.ACTIVE) {
            user.setStatus(UserStatus.LOCKED);
        } else {
            user.setStatus(UserStatus.ACTIVE);
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private Set<Role> resolveRoles(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String name : roleNames) {
            Role role = roleRepository.findByName(name).orElseThrow();
            roles.add(role);
        }
        return roles;
    }
}
