package org.example.mybooklibrary.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mybooklibrary.user.User;
import org.example.mybooklibrary.book.Books;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    private Integer rating;
    private String comment;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @PrePersist
    protected void onCreate() {
        this.reviewDate = LocalDateTime.now();
    }

}
