package org.example.mybooklibrary.favorite;

import jakarta.persistence.*;
import lombok.Data;
import org.example.mybooklibrary.book.Books;
import org.example.mybooklibrary.user.User;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
