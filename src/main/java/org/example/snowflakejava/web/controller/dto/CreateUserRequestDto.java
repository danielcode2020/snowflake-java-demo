package org.example.snowflakejava.web.controller.dto;

import java.util.List;

public record CreateUserRequestDto(String reason,
                                   String allocationPeriodType,
                                   List<SourceAndYearDto> sourceIdYears,
                                   List<Integer> months) {
}

