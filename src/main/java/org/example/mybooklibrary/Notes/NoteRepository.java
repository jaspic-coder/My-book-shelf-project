package org.example.mybooklibrary.Notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {


         List<Note> findByUserId(Long userId);
        List<Note> findByBookId(Long bookId);
    }

