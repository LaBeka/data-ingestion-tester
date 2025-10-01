package com.example.data_quality.api.dto;

import java.util.List;

public record ValidationReportDto(
        String jobId,
        List<FileValidationResultDto> results
) {

}
