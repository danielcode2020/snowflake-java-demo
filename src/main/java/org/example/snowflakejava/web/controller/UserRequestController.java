package org.example.snowflakejava.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.example.snowflakejava.web.controller.dto.CreateUserRequestDto;
import org.example.snowflakejava.repository.UserRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-request")
public class UserRequestController {

    private final UserRequestRepository userRequestRepository;

    public UserRequestController(UserRequestRepository userRequestRepository) {
        this.userRequestRepository = userRequestRepository;
    }

    @Operation(
            summary = "Invoke int procedure with string uuid as return",
            description = """
                    Goal is to test exception handling between java and snowflake
                    """)
    @GetMapping("/call-int-stored-procedure")
    public ResponseEntity<UUID> callInt(@RequestParam Integer value) {
        UUID response = userRequestRepository.callInt(value);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Invoke string procedure with string as return",
            description = """
                    
                    """)
    @GetMapping("/call-string-stored-procedure")
    public ResponseEntity<UUID> callString(@RequestParam String value) {
        UUID response = userRequestRepository.callString(value);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/call-int-array-stored-procedure")
    public ResponseEntity<UUID> callIntArrayStoredProcedure(@RequestParam int[] value) {
        UUID response = userRequestRepository.callIntArrayStoredProcedure(value);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/call-int-array-sql")
    public ResponseEntity<UUID> callIntArraySql(@RequestParam int[] value) {
        UUID response = userRequestRepository.callIntArraySql(value);
        return ResponseEntity.ok(response);
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
    UUID response = userRequestRepository.createUserRequestStoredProcedure(dto.reason(), dto.allocationPeriodType(), dto.sourceIdYears(), dto.months());
    return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Save userRequest using sql",
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
    @PostMapping("/create-using-sql")
    public ResponseEntity<UUID> createUserRequestSql(@RequestBody CreateUserRequestDto dto) throws JsonProcessingException {
        UUID response = userRequestRepository.createUserRequestSql(dto.reason(), dto.allocationPeriodType(), dto.sourceIdYears(), dto.months());
        return ResponseEntity.ok(response);
    }
}
