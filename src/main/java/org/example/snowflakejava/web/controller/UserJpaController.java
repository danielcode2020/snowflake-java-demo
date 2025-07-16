package org.example.snowflakejava.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.snowflakejava.domain.User;
import org.example.snowflakejava.repository.UserJpaRepository;
import org.example.snowflakejava.repository.UserJpaSpecificationRepository;
import org.example.snowflakejava.web.controller.dto.CreateUserRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jpa")
@Slf4j
@AllArgsConstructor
public class UserJpaController {

    private final UserJpaRepository userJpaRepository;
    private final UserJpaSpecificationRepository userJpaSpecificationRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/users")
    public List<User> getAllUsersJpa() {
        log.debug("Request to fetch all jpa users");
        return userJpaRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.debug("Request to fetch user by id: {}", id);
        return userJpaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUserJpa(@RequestBody User user) {
        log.debug("Request to save jpa user : {} ", user);
        return ResponseEntity.ok(userJpaRepository.save(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUserJpa(@PathVariable Long id) {
        log.debug("Request to delete jpa user by id: {}", id);
        userJpaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/call-int")
    public ResponseEntity<UUID> callInt(@RequestParam Integer value) {
        UUID response = UUID.fromString(userJpaRepository.callInt(value));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/call-int-array")
    public ResponseEntity<UUID> callIntArray(@RequestParam Integer[] value) {
        UUID response = UUID.fromString(userJpaRepository.callInt(value));
        return ResponseEntity.ok(response);
    }


    @GetMapping("/filter")
    public ResponseEntity<List<User>> filter() {
        Specification<User> specification = Specification.where(null);
        specification = specification.and((root, query, builder) -> builder.equal(root.get("login"), "daniel"));
        var list = userJpaSpecificationRepository.findAll(specification, Pageable.ofSize(4));
        return ResponseEntity.ok(list.getContent());
    }

    @Operation(
            summary = "Save userRequest using stored procedure",
            description = """
                    Json for testing:
                    ```
                    {
                      "reason": "CancelledPlans",
                      "allocationPeriodType": "Quartely",
                      "sourceIdYears": [
                        {
                          "year": "2021",
                          "source": "Radio"
                        },
                        {
                          "year": "2022",
                          "source": "TV"
                        }
                      ],
                      "months": [
                        0,1,2,3,4
                      ]
                    }
                    ```
                    """)
    @PostMapping("/create-using-stored-procedure")
    public ResponseEntity<UUID> createUserRequestStoredProcedure(@RequestBody CreateUserRequestDto dto) throws JsonProcessingException {
        UUID response = UUID.fromString(userJpaRepository.callCreateUserRequest(dto.reason(),
                dto.allocationPeriodType(),
                objectMapper.writeValueAsString( dto.sourceIdYears()),
                objectMapper.writeValueAsString(dto.months())));
        return ResponseEntity.ok(response);
    }
}
