package org.example.mybooklibrary.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
