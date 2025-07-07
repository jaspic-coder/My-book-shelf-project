package org.example.mybooklibrary.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("437575219791363")
    private String API_KEY;
    @Value("op0J0IZY7zwx1yz19")
    private String API_SECRET;
    @Value("${cloudinary.cloud_name}")
    private String CLOUD_NAME;

    @Bean
    Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", CLOUD_NAME,
                        "api_key", API_KEY,
                        "api_secret", API_SECRET
                )
        );
    }
}
