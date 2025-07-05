package org.example.mybooklibrary.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponse createBook(BookRequest request) {
        Books book = new Books();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setISBN(request.getIsbn());
        book.setPublishDate(request.getPublishDate());
        book.setCategory(request.getCategory());
        book.setAvailabilityStatus(true);

        book = bookRepository.save(book);
        return mapToResponse(book);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToResponse(book);
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setISBN(request.getIsbn());
        book.setPublishDate(request.getPublishDate());
        book.setCategory(request.getCategory());

        book = bookRepository.save(book);
        return mapToResponse(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private BookResponse mapToResponse(Books book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getISBN(),
                null, // You don't have a publisher field in Books entity
                book.getPublishDate(),
                book.getCategory(),
                book.getAvailabilityStatus()
        );
    }

    public String uploadCoverFromUrl(Long bookId, String imageUrl) throws IOException {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        String fileName = "cover_" + bookId + "_" + System.currentTimeMillis() + ".jpg";
        String uploadDir = "uploads/";
        Path destination = Paths.get(uploadDir, fileName);

        Files.createDirectories(destination.getParent());

        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace(); // Log the actual error
            throw new IOException("Could not download image from URL: " + imageUrl);
        }

        book.setCoverImageUrl(uploadDir + fileName);
        bookRepository.save(book);

        return "Cover image downloaded and linked successfully.";
    }
}