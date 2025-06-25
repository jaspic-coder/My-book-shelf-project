package org.example.mybooklibrary.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Favorite> addFavorite(@RequestParam Long userId, @PathVariable Long bookId) {
        Favorite favorite = favoriteService.addFavorite(userId, bookId);
        return ResponseEntity.ok(favorite);
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getFavorites(@RequestParam Long userId) {
        List<Favorite> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }
}