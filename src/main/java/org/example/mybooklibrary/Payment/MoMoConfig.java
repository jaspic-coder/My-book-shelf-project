package org.example.mybooklibrary.Payment;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "momo.api")
public class MoMoConfig {
    private String key;
    private String url;

    // Getters and setters
    public String getApiKey() {
        return key;
    }

    public void setApiKey(String key) {
        this.key = key;
    }

    public String getApiUrl() {
        return url;
    }

    public void setApiUrl(String url) {
        this.url = url;
    }
}