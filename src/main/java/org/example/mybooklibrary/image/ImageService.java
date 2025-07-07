package org.example.mybooklibrary.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final Cloudinary cloudinary;

    public CompletableFuture<List<Map<String, String>>> upload(MultipartFile[] files, String bucketName) {
        List<CompletableFuture<Map<String, String>>> uploadFutures = Arrays.stream(files)
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        var params = ObjectUtils.asMap(
                                "public_id", bucketName + "/" + file.getOriginalFilename(),
                                "folder", bucketName,
                                "use_uniquename", "true"
                        );

                        var cloudinaryResponse = cloudinary.uploader().upload(file.getBytes(), params);
                        String url = cloudinaryResponse.get("secure_url").toString();
                        String publicId = cloudinaryResponse.get("public_id").toString();
                        return Map.of("url", url, "identifier", publicId);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
                    }
                }))
                .toList();

        return CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> uploadFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));

    }

}