package com.example.data_quality.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="upload_job")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UploadJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // or UUID generator
    @Column(length = 128, name="id")
    private UUID id;

    @Column(name = "created_at", nullable=false)
    private Instant createdAt;

    @Column(name = "finished_at", nullable=false)
    private Instant finishedAt;

    @Column(name = "status", nullable=false)
    private String status;

    @Column(name="note", nullable=false)
    private String note;

}
