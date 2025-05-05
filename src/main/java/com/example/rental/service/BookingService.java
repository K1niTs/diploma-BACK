// src/main/java/com/example/rental/service/BookingService.java
package com.example.rental.service;

import com.example.rental.dto.BookingDto;
import com.example.rental.entity.*;
import com.example.rental.repo.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepo    repo;
    private final InstrumentRepo instruments;
    private final UserRepo       users;

    public BookingService(BookingRepo r, InstrumentRepo i, UserRepo u) {
        repo        = r;
        instruments = i;
        users       = u;
    }

    /* ------------ СОЗДАНИЕ (NEW) ------------ */
    public BookingDto create(BookingDto dto) {

        Instrument instr = instruments.findById(dto.instrumentId()).orElseThrow();
        User       user  = users.findById(dto.userId()).orElseThrow();

        long days = ChronoUnit.DAYS.between(dto.startDate(), dto.endDate());
        if (days <= 0) throw new RuntimeException("endDate must be after startDate");

        long overlaps = repo.countOverlaps(
                dto.instrumentId(), dto.startDate(), dto.endDate());
        if (overlaps > 0)
            throw new RuntimeException("Инструмент уже забронирован на эти даты");

        double cost = days * instr.getPricePerDay();

        Booking b = new Booking();
        b.setInstrument(instr);
        b.setUser(user);
        b.setStartDate(dto.startDate());
        b.setEndDate(dto.endDate());
        b.setTotalCost(cost);
        b.setStatus("NEW");
        // paymentUrl остаётся null
        repo.save(b);

        return map(b);
    }

    /* ------------ ОПЛАТА (PAY) --------------- */
    public BookingDto pay(Long id, Long uid) {

        Booking b = repo.findById(id).orElseThrow();

        if (!b.getUser().getId().equals(uid))
            throw new AccessDeniedException("not yours");

        if (!"NEW".equals(b.getStatus()))
            throw new RuntimeException("already paid / cancelled");

        /* здесь можно вызвать настоящий PSP;
           пока – фиктивная ссылка */
        String url = "https://pay.demo/mock?order=" + b.getId();

        b.setPaymentUrl(url);
        b.setStatus("WAITING_PAYMENT");
        repo.save(b);

        return map(b);
    }

    /* ------------ ЗАКРЫТЬ (CANCEL) ----------- */
    public BookingDto cancel(Long id, Long uid){
        Booking b = repo.findById(id).orElseThrow();
        if (!b.getUser().getId().equals(uid))
            throw new AccessDeniedException("not yours");
        b.setStatus("CANCELLED");
        repo.save(b);
        return map(b);
    }

    /* ------------ READ helpers --------------- */
    public List<BookingDto> all()             { return repo.findAll()          .stream().map(this::map).toList(); }
    public List<BookingDto> listByUser(Long u){ return repo.findByUserId(u)    .stream().map(this::map).toList(); }

    /* ------------ MAP ------------------------ */
    private BookingDto map(Booking b){
        return new BookingDto(
                b.getId(),
                b.getUser().getId(),
                b.getInstrument().getId(),
                b.getInstrument().getTitle(),
                b.getStartDate(),
                b.getEndDate(),
                b.getTotalCost(),
                b.getStatus(),
                b.getPaymentUrl()        // ← вернётся null или ссылка
        );
    }
}
