package com.example.data_quality.api.dto;

public record ValidationError(
        String code,
        String message,
        Integer line,
        String field
) {

}

