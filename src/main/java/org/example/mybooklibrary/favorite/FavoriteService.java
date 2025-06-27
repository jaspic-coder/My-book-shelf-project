package org.example.mybooklibrary.favorite;

import org.example.mybooklibrary.book.Books;
import org.example.mybooklibrary.book.BookRepository;
import org.example.mybooklibrary.user.User;
import org.example.mybooklibrary.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Favorite addFavorite(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new RuntimeException("Book already in favorites");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setBook(book);
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
}