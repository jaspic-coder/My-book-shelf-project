package org.example.mybooklibrary.Borrow;

import org.example.mybooklibrary.book.BookRepository;
import org.example.mybooklibrary.book.Books;
import org.example.mybooklibrary.user.User;
import org.example.mybooklibrary.user.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowService(BorrowRepository borrowRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Borrows borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Books books = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Borrows borrows = new Borrows();
        borrows.setUser(user);
        borrows.setBooks(books);
        borrows.setBorrowDate(LocalDateTime.now());
        borrows.setStatus("BORROWED");

        return borrowRepository.save(borrows);
    }

    public List<Borrows> getAllBorrows() {
        return borrowRepository.findAll();
    }

    public Optional<Borrows> getBorrowById(Long id) {
        return borrowRepository.findById(id);
    }

    public Borrows returnBook(Long borrowId) {
        Borrows borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow not found"));

        borrow.setReturnDate(LocalDateTime.now());
        borrow.setStatus("RETURNED");

        return borrowRepository.save(borrow);
    }
}
