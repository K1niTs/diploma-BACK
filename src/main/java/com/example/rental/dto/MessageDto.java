package com.example.rental.dto;

import java.time.Instant;

public record MessageDto(
        Long    id,
        Long    fromId,
        Long    toId,
        String  text,
        Instant createdAt,
        String  fromEmail
) {}
