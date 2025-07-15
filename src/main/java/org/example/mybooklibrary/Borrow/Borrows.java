package org.example.mybooklibrary.Borrow;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.mybooklibrary.Payment.Payments;
import org.example.mybooklibrary.book.Books;
import org.example.mybooklibrary.user.User;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "borrows")
public class Borrows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "book_id", nullable = false)
//    private Books bookId;

    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    private String status;
    @ManyToOne
    @JoinColumn(name = "books_id")
    private Books books;
    private String paymentId;
    private String paymentStatus;
    @ManyToOne
private Payments payments;
}
