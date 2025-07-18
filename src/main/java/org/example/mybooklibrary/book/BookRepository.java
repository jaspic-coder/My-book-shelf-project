package org.example.mybooklibrary.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByIsbn(String isbn);
}
