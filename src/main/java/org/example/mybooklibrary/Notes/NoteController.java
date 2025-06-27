package org.example.mybooklibrary.Notes;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

    @RestController
    @RequestMapping("/api/notes")
    public class NoteController {

        @Autowired
        private NoteService noteService;

        @PostMapping
        public Note createNote(@RequestBody Note note) {
            return noteService.createNote(note);
        }

        @GetMapping("/user/{userId}")
        public List<Note> getNotesByUser(@PathVariable Long userId) {
            return noteService.getNotesByUserId(userId);
        }

        @GetMapping("/book/{bookId}")
        public List<Note> getNotesByBook(@PathVariable Long bookId) {
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


