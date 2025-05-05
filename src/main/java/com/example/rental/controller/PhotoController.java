// src/main/java/com/example/rental/controller/PhotoController.java
package com.example.rental.controller;

import com.example.rental.dto.InstrumentDto;
import com.example.rental.entity.Instrument;
import com.example.rental.repo.InstrumentRepo;
import org.apache.commons.io.FilenameUtils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

/**
 * Загрузка / замена фотографии инструмента.
 * После успешного аплоада возвращает обновлённый InstrumentDto
 * с ПОЛНЫМ URL до изображения (например http://192.168.0.41:8081/photos/abc.jpg),
 * чтобы мобильный клиент смог сразу отобразить картинку.
 */
@RestController
@RequestMapping("/instruments")
public class PhotoController {

    private final InstrumentRepo repo;
    private final Path           photoRoot;     // …/static/photos

    /** если приложение развернуто не в корне («/api» и т.д.) */
    @Value("${server.servlet.context-path:}")
    private String ctxPath;

    public PhotoController(InstrumentRepo repo, Path photoRoot) {
        this.repo      = repo;
        this.photoRoot = photoRoot;
    }

    /* ------------------------------------------------------------------ */

    @PostMapping("/{id}/photo")
    public InstrumentDto upload(@PathVariable Long id,
                                @RequestPart("file") MultipartFile file,
                                HttpServletRequest req) throws IOException {

        Instrument inst = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));

        /* ---------- 1. Генерируем уникальное имя файла ---------- */
        String ext  = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = UUID.randomUUID() + (ext.isBlank() ? ".jpg" : "." + ext);

        Path dst = photoRoot.resolve(name);          // …/static/photos/<name>

        /* ---------- 2. Сохраняем файл на диск ---------- */
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dst, StandardCopyOption.REPLACE_EXISTING);
        }

        /* ---------- 3. Формируем ПОЛНЫЙ URL ---------- */
        String base = req.getScheme() + "://" +          // http://
                req.getServerName() +              // 192.168.0.41
                ":" + req.getServerPort();         // :8081

        // учитываем, что приложение может жить, например, под "/api"
        String url = base + ctxPath + "/photos/" + name; // http://…/photos/xxx.jpg

        /* ---------- 4. Сохраняем URL в сущности ---------- */
        inst.setImageUrl(url);
        repo.save(inst);

        /* ---------- 5. Возвращаем обновлённый DTO ---------- */
        return new InstrumentDto(
                inst.getId(),
                inst.getOwner().getId(),
                inst.getTitle(),
                inst.getDescription(),
                inst.getPricePerDay(),
                inst.getCategory(),
                inst.getImageUrl()          // уже полный URL
        );
    }
}
