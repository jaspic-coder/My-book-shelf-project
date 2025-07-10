package org.example.mybooklibrary.book;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/files")
@Tag(name = "PDF Upload", description = "API for uploading PDF files")
public class FileUploadController {

    private final String uploadDir = "uploads/";

    @PostMapping("/upload-pdf")
    @Operation(summary = "Upload a PDF", description = "Uploads a PDF file to the server")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed.");
        }

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            return ResponseEntity.ok("PDF uploaded successfully to: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload PDF: " + e.getMessage());
        }
    }
}




