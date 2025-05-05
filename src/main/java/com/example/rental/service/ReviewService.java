// src/main/java/com/example/rental/service/ReviewService.java
package com.example.rental.service;

import com.example.rental.dto.ReviewDto;
import com.example.rental.entity.*;
import com.example.rental.repo.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepo    reviews;
    private final BookingRepo   bookings;
    private final InstrumentRepo instruments;
    private final UserRepo      users;

    public ReviewService(ReviewRepo r, BookingRepo b,
                         InstrumentRepo i, UserRepo u){
        reviews     = r;
        bookings    = b;
        instruments = i;
        users       = u;
    }

    /* ---------- список для инструмента ---------- */
    public List<ReviewDto> listForInstrument(Long instrId){
        return reviews.findByInstrument_Id(instrId)
                .stream().map(this::map)
                .toList();
    }

    /* ---------- добавить ---------- */
    public ReviewDto add(Long instrId, Long uid, int rating, String comment){

        if(rating < 1 || rating > 5)
            throw new IllegalArgumentException("rating 1..5");

        /* уже оставлял? */
        if(reviews.existsByUser_IdAndInstrument_Id(uid, instrId))
            throw new RuntimeException("Already reviewed");

        /* есть оплаченная/ожидающая оплата бронь? */
        boolean ok = bookings.existsByUser_IdAndInstrument_IdAndStatusIn(
                uid, instrId, List.of("WAITING_PAYMENT","PAID"));
        if(!ok) throw new RuntimeException("Need paid booking first");

        Review r = new Review();
        r.setUser(      users.findById(uid).orElseThrow());
        r.setInstrument(instruments.findById(instrId).orElseThrow());
        r.setRating(rating);
        r.setComment(comment == null ? "" : comment);
        r.setCreatedAt(Instant.now());
        reviews.save(r);

        return map(r);
    }

    /* ---------- mapper ---------- */
    private ReviewDto map(Review r){
        return new ReviewDto(
                r.getId(),
                r.getUser().getId(),
                r.getUser().getEmail(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt());
    }
}
