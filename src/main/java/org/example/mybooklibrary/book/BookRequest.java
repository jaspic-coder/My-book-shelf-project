package org.example.mybooklibrary.book;

import lombok.Data;

import java.time.LocalDate;
@Data
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishDate;
    private String category;


}
