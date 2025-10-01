package com.example.data_quality.core.validators;

import com.example.data_quality.api.dto.FileValidationResultDto;
import com.example.data_quality.api.dto.ValidationError;
import com.example.data_quality.core.FileType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.util.*;

@Component
public class XmlValidator implements FileValidator {
    @Override public boolean supports(FileType type) { return type == FileType.XML; }

    @Override
    public FileValidationResultDto validate(MultipartFile file) {
        List<ValidationError> errors = new ArrayList<>();
        try {
            var xsd = new ClassPathResource("schemas/invoice.xsd"); // replace with your XSD
            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            var schema = factory.newSchema(xsd.getURL());
            var validator = schema.newValidator();
            validator.validate(new StreamSource(file.getInputStream()));
            return new FileValidationResultDto(file.getOriginalFilename(), true, "Valid XML", errors);
        } catch (Exception e) {
            errors.add(new ValidationError("XSD", e.getMessage(), null, null));
            return new FileValidationResultDto(file.getOriginalFilename(), false, "Invalid XML", errors);
        }
    }
}