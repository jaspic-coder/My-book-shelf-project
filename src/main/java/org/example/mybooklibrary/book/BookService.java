package org.example.mybooklibrary.book;
import lombok.RequiredArgsConstructor;
import org.example.mybooklibrary.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " is not here"));
        return mapToResponse(book);
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " is not here"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setISBN(request.getIsbn());
        book.setPublishDate(request.getPublishDate());
        book.setCategory(request.getCategory());

        book = bookRepository.save(book);
        return mapToResponse(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book with ID " + id + " is not here");
        }
        bookRepository.deleteById(id);
    }

    private BookResponse mapToResponse(Books book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getISBN(),
                null,
                book.getPublishDate(),
                book.getCategory(),
                book.getAvailabilityStatus()
                );
    }
}