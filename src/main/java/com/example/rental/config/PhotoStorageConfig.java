// src/main/java/com/example/rental/config/PhotoStorageConfig.java
package com.example.rental.config;

import org.springframework.beans.factory.annotation.Value;   // ← правильный импорт
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.*;

@Configuration
public class PhotoStorageConfig {

    @Value("${photo.dir:photos}")        // ok
    private String photoDir;

    @Bean
    public Path photoRoot() throws IOException {
        Path root = Paths.get("static").resolve(photoDir);
        Files.createDirectories(root);
        return root;
    }
}
