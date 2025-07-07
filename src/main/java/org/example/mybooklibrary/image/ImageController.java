package org.example.mybooklibrary.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/api/upload", consumes = "multipart/form-data")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService service;

    @PostMapping
    @Operation(summary = "Upload images to cloudinary")
    public CompletableFuture<ResponseEntity<List<Map<String, String>>>> uploadImage(
            @Parameter(
                    description = "Images to upload",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "array", format = "binary")
                    )
            )
            @RequestPart("files") MultipartFile[] files
    ) {
        return service.upload(files, "car_finder_folder")
                .thenApply(ResponseEntity::ok);
    }
}