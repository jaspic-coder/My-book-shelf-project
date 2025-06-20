package org.example.mybooklibrary.Borrow;
gi
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/borrow")
    public class BorrowController {

        private final BorrowService borrowService;

        public BorrowController(BorrowService borrowService) {
            this.borrowService = borrowService;
        }

        @PostMapping("/borrow")
        public ResponseEntity<Borrows> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
            return ResponseEntity.ok(borrowService.borrowBook(userId, bookId));
        }

        @GetMapping
        public ResponseEntity<List<Borrows>> getAllBorrows() {
            return ResponseEntity.ok(borrowService.getAllBorrows());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Borrows> getBorrow(@PathVariable Long id) {
            return borrowService.getBorrowById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping("/return/{id}")
        public ResponseEntity<Borrows> returnBook(@PathVariable Long id) {
            return ResponseEntity.ok(borrowService.returnBook(id));
        }
    }


