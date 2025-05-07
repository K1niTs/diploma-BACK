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


@RestController
@RequestMapping("/instruments")
public class PhotoController {

    private final InstrumentRepo repo;
    private final Path           photoRoot;

    @Value("${server.servlet.context-path:}")
    private String ctxPath;

    public PhotoController(InstrumentRepo repo, Path photoRoot) {
        this.repo      = repo;
        this.photoRoot = photoRoot;
    }


    @PostMapping("/{id}/photo")
    public InstrumentDto upload(@PathVariable Long id,
                                @RequestPart("file") MultipartFile file,
                                HttpServletRequest req) throws IOException {

        Instrument inst = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));

        String ext  = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = UUID.randomUUID() + (ext.isBlank() ? ".jpg" : "." + ext);

        Path dst = photoRoot.resolve(name);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dst, StandardCopyOption.REPLACE_EXISTING);
        }

        String base = req.getScheme() + "://" +
                req.getServerName() +
                ":" + req.getServerPort();

        String url = base + ctxPath + "/photos/" + name;

        inst.setImageUrl(url);
        repo.save(inst);

        return new InstrumentDto(
                inst.getId(),
                inst.getOwner().getId(),
                inst.getTitle(),
                inst.getDescription(),
                inst.getPricePerDay(),
                inst.getCategory(),
                inst.getImageUrl()
        );
    }
}
