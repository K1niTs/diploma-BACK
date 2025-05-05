// src/main/java/com/example/rental/controller/BookingController.java
package com.example.rental.controller;

import com.example.rental.dto.BookingDto;
import com.example.rental.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService svc;
    public BookingController(BookingService s){ svc = s; }

    /* -------- все (debug/admin) -------- */
    @GetMapping
    public List<BookingDto> list(){ return svc.all(); }

    /* -------- создать -------- */
    @PostMapping
    public BookingDto create(@RequestBody BookingDto dto,
                             HttpServletRequest req){

        Long uid = (Long) req.getAttribute("uid");
        if (uid == null) throw new RuntimeException("Auth required");

        /* id, title, cost, status, paymentUrl заполняет сервис */
        BookingDto toCreate = new BookingDto(
                null,
                uid,
                dto.instrumentId(),
                null,
                dto.startDate(),
                dto.endDate(),
                0,
                "NEW",
                null
        );
        return svc.create(toCreate);
    }

    /* -------- мои -------- */
    @GetMapping("/my")
    public List<BookingDto> my(HttpServletRequest req){
        Long uid = (Long) req.getAttribute("uid");
        return svc.listByUser(uid);
    }

    /* -------- оплатить -------- */
    @PostMapping("/{id}/pay")
    public BookingDto pay(@PathVariable Long id,
                          HttpServletRequest req){
        Long uid = (Long) req.getAttribute("uid");
        return svc.pay(id, uid);
    }

    /* -------- отменить -------- */
    @PatchMapping("/{id}/cancel")
    public BookingDto cancel(@PathVariable Long id,
                             HttpServletRequest req){
        Long uid = (Long) req.getAttribute("uid");
        return svc.cancel(id, uid);
    }
}
