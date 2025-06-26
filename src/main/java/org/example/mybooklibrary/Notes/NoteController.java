package org.example.mybooklibrary.Notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<String> createNote(@RequestBody Note note) {
        noteService.saveNote(note); // Make sure you have this method in NoteService
        return ResponseEntity.status(HttpStatus.CREATED).body("Note added successfully.");
    }

    @GetMapping("/user/{userId}")
    public List<Note> getNotesByUser(@PathVariable Long userId) {
        return noteService.getNotesByUserId(userId);
    }

    @GetMapping("/book/{bookId}")
    public List<Note> getNotesByBookId(@PathVariable Long bookId) {
        return noteService.getNotesByBookId(bookId);
    }

    @GetMapping("/{id}")
    public Optional<Note> getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody String content) {
        return noteService.updateNote(id, content);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }
}
