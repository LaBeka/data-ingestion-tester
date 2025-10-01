package com.example.data_quality.api.dto;

import java.util.List;

public record FileValidationResultDto(
        String fileName,
        boolean success,
        String message,
        List<ValidationError> errors
) {

}

