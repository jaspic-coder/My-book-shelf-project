package org.example.mybooklibrary.book;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String ISBN;
    private LocalDate publishDate;
    private String category;
    private Boolean availabilityStatus;

}


