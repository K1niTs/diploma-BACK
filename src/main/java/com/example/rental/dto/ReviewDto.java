package com.example.rental.dto;

import java.time.Instant;

public record ReviewDto(
        Long    id,
        Long    userId,
        String  userName,
        int     rating,
        String  comment,
        Instant createdAt) {}
