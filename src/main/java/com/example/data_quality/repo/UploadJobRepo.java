package com.example.data_quality.repo;

import com.example.data_quality.model.UploadJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UploadJobRepo extends JpaRepository<UploadJob, UUID> {}