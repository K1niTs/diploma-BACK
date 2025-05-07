package com.example.rental.service;

import com.example.rental.dto.InstrumentDto;
import com.example.rental.entity.Instrument;
import com.example.rental.repo.InstrumentRepo;
import com.example.rental.repo.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class InstrumentService {

    private final InstrumentRepo repo;
    private final UserRepo users;

    public InstrumentService(InstrumentRepo repo, UserRepo users) {
        this.repo  = repo;
        this.users = users;
    }

    public InstrumentDto create(InstrumentDto dto) {
        Instrument inst = new Instrument();
        inst.setOwner(users.findById(dto.ownerId()).orElseThrow());
        inst.setTitle(dto.title());
        inst.setDescription(dto.description());
        inst.setPricePerDay(dto.pricePerDay());
        inst.setCategory(dto.category());
        inst.setImageUrl(dto.imageUrl());
        repo.save(inst);
        return map(inst);
    }

    public InstrumentDto update(Long id, InstrumentDto dto, Long uid) {
        Instrument inst = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));
        if (!inst.getOwner().getId().equals(uid)) {
            throw new AccessDeniedException("Not yours");
        }
        inst.setTitle(dto.title());
        inst.setDescription(dto.description());
        inst.setPricePerDay(dto.pricePerDay());
        inst.setCategory(dto.category());
        repo.save(inst);
        return map(inst);
    }

    public void delete(Long id, Long uid) {
        Instrument inst = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));
        if (!inst.getOwner().getId().equals(uid)) {
            throw new AccessDeniedException("Not yours");
        }
        repo.delete(inst);
    }

    public Page<InstrumentDto> search(String q,
                                      String category,
                                      Double minPrice,
                                      Double maxPrice,
                                      Pageable pageable) {
        Specification<Instrument> spec = Specification
                .where(InstrumentSpec.titleLike(q))
                .and(InstrumentSpec.hasCategory(category))
                .and(InstrumentSpec.priceBetween(minPrice, maxPrice));
        return repo.findAll(spec, pageable)
                .map(this::map);
    }

    private InstrumentDto map(Instrument i) {
        return new InstrumentDto(
                i.getId(),
                i.getOwner().getId(),
                i.getTitle(),
                i.getDescription(),
                i.getPricePerDay(),
                i.getCategory(),
                i.getImageUrl()
        );
    }
}
