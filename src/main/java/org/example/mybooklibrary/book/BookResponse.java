package org.example.mybooklibrary.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;  // keep null or add in future
    private LocalDate publishDate;
    private String category;
    private Boolean available;
    private Integer rating;
    private String coverUrl;
    private String bookUrl;
}
