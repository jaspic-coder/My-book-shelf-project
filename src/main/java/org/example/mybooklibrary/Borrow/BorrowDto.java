package org.example.mybooklibrary.Borrow;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BorrowDto {
    private String id;
    private String title;
    private String author;
    private LocalDateTime borrowed;
    private LocalDateTime returned;
}

