package com.example.data_quality.model;

import com.example.data_quality.api.dto.ValidationError;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="validation_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationResult {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(length = 128, name="id")
    private UUID id;

    @Column(name="job_id", nullable=false)
    private UUID jobId;

    @Column(name="file_name", nullable=false)
    private String fileName;

    @Column(name="success", nullable=false)
    private boolean success;

    @Column(name="message", columnDefinition="text")
    private String message;

    @Type(JsonBinaryType.class)
    @Column(name="errors_json", columnDefinition = "jsonb")
    private List<ValidationError> errorsJson; // <--- mapped directly as JSONB

    @Column(name = "created_at", nullable=false)
    private Instant createdAt;
}
