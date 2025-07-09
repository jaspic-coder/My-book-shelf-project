package org.example.mybooklibrary.book;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private String publisher; // <- include this
        private LocalDate publishedDate;
        private String category;
        private Boolean availabilityStatus;
    }

