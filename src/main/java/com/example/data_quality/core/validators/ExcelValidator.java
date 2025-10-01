package com.example.data_quality.core.validators;

import com.example.data_quality.api.dto.FileValidationResultDto;
import com.example.data_quality.api.dto.ValidationError;
import com.example.data_quality.core.FileType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
public class ExcelValidator implements FileValidator {
    @Override public boolean supports(FileType type) { return type == FileType.EXCEL; }

    @Override
    public FileValidationResultDto validate(MultipartFile file) {
        List<ValidationError> errors = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            Row header = sheet.getRow(0);
            if (header == null) {
                errors.add(new ValidationError("HEADER", "Missing header row", 1, null));
            } else {
                Set<String> cols = new HashSet<>();
                header.forEach(c -> cols.add(c.getStringCellValue().trim()));
                if (!cols.contains("ID"))   errors.add(new ValidationError("MISSING_COL","Missing 'ID'",1,"ID"));
                if (!cols.contains("DATE")) errors.add(new ValidationError("MISSING_COL","Missing 'DATE'",1,"DATE"));
            }
            boolean ok = errors.isEmpty();
            return new FileValidationResultDto(file.getOriginalFilename(), ok, ok? "Valid Excel" : "Invalid Excel", errors);
        } catch (Exception e) {
            errors.add(new ValidationError("EXCEPTION", e.getMessage(), null, null));
            return new FileValidationResultDto(file.getOriginalFilename(), false, "Excel parse error", errors);
        }
    }
}
