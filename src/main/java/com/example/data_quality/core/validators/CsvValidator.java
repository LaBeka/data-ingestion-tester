package com.example.data_quality.core.validators;

import com.example.data_quality.api.dto.FileValidationResultDto;
import com.example.data_quality.api.dto.ValidationError;
import com.example.data_quality.core.FileType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class CsvValidator implements  FileValidator {
    @Override
    public boolean supports(FileType type) {
        return type == FileType.CSV;
    }

    @Override
    public FileValidationResultDto validate(MultipartFile file) {
        List<ValidationError> errors = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String header = br.readLine();
            if (header == null) {
                errors.add(new ValidationError("EMPTY", "File is empty", 1, null));
                return new FileValidationResultDto(file.getOriginalFilename(), false, "Empty CSV", errors);
            }
            // MVP rule: must contain "ID" and "DATE"
            List<String> cols = Arrays.stream(header.split(",")).map(String::trim).toList();
            if (!cols.contains("ID")) errors.add(new ValidationError("MISSING_COL", "Missing column 'ID'", 1, "ID"));
            if (!cols.contains("DATE")) errors.add(new ValidationError("MISSING_COL", "Missing column 'DATE'", 1, "DATE"));

            // quick row sanity
            String line; int row = 1;
            while ((line = br.readLine()) != null) {
                row++;
                if (line.isBlank()) errors.add(new ValidationError("EMPTY_ROW", "Blank row", row, null));
            }

            boolean ok = errors.isEmpty();
            return new FileValidationResultDto(file.getOriginalFilename(), ok, ok ? "Valid CSV" : "Invalid CSV", errors);
        } catch (Exception e) {
            errors.add(new ValidationError("EXCEPTION", e.getMessage(), null, null));
            return new FileValidationResultDto(file.getOriginalFilename(), false, "CSV parse error", errors);
        }
    }
}
