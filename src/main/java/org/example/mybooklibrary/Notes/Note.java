package org.example.mybooklibrary.Notes;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

    @Data
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor

    @Table(name = "notes")
    public class Note {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long userId; // ✅ THIS MUST MATCH EXACTLY

        private Long bookId;

        @Column(columnDefinition = "TEXT")
        private String content;

        private LocalDateTime createdAt = LocalDateTime.now();
    }


