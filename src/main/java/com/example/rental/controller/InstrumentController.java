package com.example.rental.controller;

import com.example.rental.dto.InstrumentDto;
import com.example.rental.service.InstrumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {

    private final InstrumentService service;

    public InstrumentController(InstrumentService service) {
        this.service = service;
    }

    @GetMapping
    public Page<InstrumentDto> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @PageableDefault(size = 10) Pageable pageable) {
        return service.search(q, category, minPrice, maxPrice, pageable);
    }

    @PostMapping
    public InstrumentDto create(@RequestBody InstrumentDto dto,
                                HttpServletRequest req) {
        Long uid = (Long) req.getAttribute("uid");
        if (uid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Auth required");
        }
        return service.create(new InstrumentDto(
                null,
                uid,
                dto.title(),
                dto.description(),
                dto.pricePerDay(),
                dto.category(),
                dto.imageUrl()
        ));
    }

    @PutMapping("/{id}")
    public InstrumentDto update(@PathVariable Long id,
                                @RequestBody InstrumentDto dto,
                                HttpServletRequest req) {
        Long uid = (Long) req.getAttribute("uid");
        if (uid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Auth required");
        }
        InstrumentDto toUpdate = new InstrumentDto(
                id,
                uid,
                dto.title(),
                dto.description(),
                dto.pricePerDay(),
                dto.category(),
                dto.imageUrl()
        );
        return service.update(id, toUpdate, uid);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       HttpServletRequest req) {
        Long uid = (Long) req.getAttribute("uid");
        if (uid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Auth required");
        }
        service.delete(id, uid);
    }
}
