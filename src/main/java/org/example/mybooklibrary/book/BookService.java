package org.example.mybooklibrary.book;

import lombok.RequiredArgsConstructor;
import org.example.mybooklibrary.exception.BookNotFoundException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
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



    public String uploadCoverFromUrl(Long bookId, String imageUrl) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));

        String fileName = "cover_" + bookId + "_" + Instant.now().toEpochMilli() + ".jpg";
        String uploadDir = "uploads/";
        Path destination = Paths.get(uploadDir, fileName);

        try {
            downloadImage(imageUrl, destination);
        } catch (IOException e) {
            throw new ImageDownloadException("Failed to download image from URL: " + imageUrl);
        }

        book.setCoverImageUrl(uploadDir + fileName);
        bookRepository.save(book);

        return "Cover image downloaded and linked successfully.";
    }

    private void downloadImage(String imageUrl, Path destination) throws IOException {
        Files.createDirectories(destination.getParent());

        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Optional: avoids 403 from some CDNs

        String contentType = connection.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ImageDownloadException("URL does not point to an image: " + imageUrl);
        }

        try (InputStream in = connection.getInputStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }


}