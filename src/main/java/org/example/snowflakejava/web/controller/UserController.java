package org.example.snowflakejava.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.snowflakejava.domain.User;
import org.example.snowflakejava.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.debug("Request to fetch all users");
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.debug("Request to fetch user by id: {}", id);
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.debug("Request to save user : {} ", user);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("Request to delete user by id: {}", id);
        userRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
