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

    @GetMapping
    public List<BookingDto> list(){ return svc.all(); }

    @PostMapping
    public BookingDto create(@RequestBody BookingDto dto,
                             HttpServletRequest req){

        Long uid = (Long) req.getAttribute("uid");
        if (uid == null) throw new RuntimeException("Auth required");

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

    @GetMapping("/my")
    public List<BookingDto> my(HttpServletRequest req){
        Long uid = (Long) req.getAttribute("uid");
        return svc.listByUser(uid);
    }

    @PostMapping("/{id}/pay")
    public BookingDto pay(@PathVariable Long id,
                          HttpServletRequest req){
        Long uid = (Long) req.getAttribute("uid");
        return svc.pay(id, uid);
    }

    @PatchMapping("/{id}/cancel")
    public BookingDto cancel(@PathVariable Long id,
                             HttpServletRequest req){
        Long uid = (Long) req.getAttribute("uid");
        return svc.cancel(id, uid);
    }
}
