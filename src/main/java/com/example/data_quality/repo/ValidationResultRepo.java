package com.example.data_quality.repo;

import com.example.data_quality.model.ValidationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ValidationResultRepo extends JpaRepository<ValidationResult, UUID>{

    List<ValidationResult> findByJobId(UUID jobId);
}
