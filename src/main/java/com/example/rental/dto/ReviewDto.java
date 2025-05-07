package com.example.rental.dto;

import java.time.Instant;

public record ReviewDto(
        Long    id,
        Long    userId,
        String  userName,   // e-mail или имя
        int     rating,
        String  comment,
        Instant createdAt) {}
