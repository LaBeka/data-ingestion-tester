package com.example.data_quality.core.validators;

import com.example.data_quality.api.dto.FileValidationResultDto;
import com.example.data_quality.core.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileValidator {

    boolean supports(FileType type);
    FileValidationResultDto validate(MultipartFile file);
}
