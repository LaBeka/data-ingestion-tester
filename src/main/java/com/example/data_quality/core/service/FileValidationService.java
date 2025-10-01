package com.example.data_quality.core.service;

import com.example.data_quality.api.dto.FileValidationResultDto;
import com.example.data_quality.api.dto.ValidationError;
import com.example.data_quality.api.dto.ValidationReportDto;
import com.example.data_quality.core.FileType;
import com.example.data_quality.core.validators.FileValidator;
import com.example.data_quality.model.UploadJob;
import com.example.data_quality.model.ValidationResult;
import com.example.data_quality.repo.UploadJobRepo;
import com.example.data_quality.repo.ValidationResultRepo;
import com.example.data_quality.util.FileTypeDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileValidationService {

    private final UploadJobRepo jobRepo;
    private final ValidationResultRepo resultRepo;
    private final List<FileValidator> validators; // Spring injects all @Components


    @Transactional
    public ValidationReportDto validateFiles(List<MultipartFile> files) {
        UploadJob uploadJob = UploadJob.builder()
                .createdAt(Instant.now())
                .status("RUNNING")
                .build();

        UploadJob saved = jobRepo.save(uploadJob);

        List<FileValidationResultDto> results = new ArrayList<>();
        for (MultipartFile f : files) {
            FileType type = FileTypeDetector.detect(f);
            FileValidator v = validators.stream().filter(val -> val.supports(type)).findFirst().orElse(null);
            FileValidationResultDto dto = (v == null)
                    ? new FileValidationResultDto(f.getOriginalFilename(), false, "Unsupported file type", List.of())
                    : v.validate(f);

            ValidationResult validRes = ValidationResult.builder()
                    .jobId(saved.getId())
                    .fileName(dto.fileName())
                    .success(dto.success())
                    .message(dto.message())
                    .errorsJson(dto.errors())  // simple helper below
                    .createdAt(Instant.now())
                    .build();
            // persist each file result
            resultRepo.save(validRes);

            results.add(dto);
        }

        jobRepo.findById(saved.getId()).ifPresent(j -> { j.setStatus("DONE"); j.setFinishedAt(Instant.now()); });
        return new ValidationReportDto(saved.getId().toString(), results);
    }

    @Transactional(readOnly = true)
    public ValidationReportDto getReport(UUID jobId) {
        var items = resultRepo.findByJobId(jobId);
        var dtos = items.stream().map(vr ->
                new FileValidationResultDto(
                        vr.getFileName(),
                        vr.isSuccess(),
                        vr.getMessage(),
                        vr.getErrorsJson())
        ).collect(Collectors.toList());
        return new ValidationReportDto(jobId.toString(), dtos);
    }

    // tiny JSON helper (use Jackson)
    static final class Jsons {

        private static final com.fasterxml.jackson.databind.ObjectMapper M = new com.fasterxml.jackson.databind.ObjectMapper();

        static String toJson(Object o){ try { return M.writeValueAsString(o);} catch(Exception e){return "[]";}}

        static List<ValidationError> fromJsonArray(String s){
            try {
                var t = M.getTypeFactory().constructCollectionType(List.class, ValidationError.class);
                return M.readValue(s==null?"[]":s, t);
            } catch(Exception e){ return List.of(); }
        }
    }
}
