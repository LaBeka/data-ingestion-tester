package com.example.data_quality.api.controller;
import com.example.data_quality.api.dto.ValidationReportDto;
import com.example.data_quality.core.service.FileValidationService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileValidationService service;

    @PostMapping(value="/upload", consumes="multipart/form-data")
    public ResponseEntity<ValidationReportDto> upload(
            @RequestParam("files")
            @NotEmpty List<MultipartFile> files
    ) {
        return ResponseEntity.ok(service.validateFiles(files));
    }

    @GetMapping("/report/{jobId}")
    public ResponseEntity<ValidationReportDto> report(@PathVariable String jobId) {
        return ResponseEntity.ok(service.getReport(UUID.fromString(jobId)));
    }
}
