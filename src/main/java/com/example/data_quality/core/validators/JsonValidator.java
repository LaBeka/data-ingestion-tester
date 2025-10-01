package com.example.data_quality.core.validators;

import com.example.data_quality.api.dto.FileValidationResultDto;
import com.example.data_quality.api.dto.ValidationError;
import com.example.data_quality.core.FileType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
public class JsonValidator implements FileValidator{

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean supports(FileType type) {
        return type == FileType.JSON;
    }

    @Override
    public FileValidationResultDto validate(MultipartFile file) {
        List<ValidationError> errors = new ArrayList<>();

        try {
            JsonNode root = MAPPER.readTree(file.getInputStream());
            // MVP: validate either object or array of objects with required fields ["id","date"]
            List<String> required = List.of("id", "date");
            if (root.isArray()) {
                int idx = 0;
                for (JsonNode n : root) {
                    idx++;
                    for (String r : required) {
                        if (!n.hasNonNull(r)) errors.add(new ValidationError("REQUIRED", "Missing "+r, idx, r));
                    }
                }
            } else if (root.isObject()) {
                for (String r : required) if (!root.hasNonNull(r)) errors.add(new ValidationError("REQUIRED","Missing "+r, 1, r));
            } else {
                errors.add(new ValidationError("FORMAT", "JSON must be object or array", 1, null));
            }
            boolean ok = errors.isEmpty();
            return new FileValidationResultDto(file.getOriginalFilename(), ok, ok? "Valid JSON":"Invalid JSON", errors);
        } catch (Exception e) {
            errors.add(new ValidationError("EXCEPTION", e.getMessage(), null, null));
            return new FileValidationResultDto(file.getOriginalFilename(), false, "JSON parse error", errors);
        }
    }
}
