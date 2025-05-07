package com.example.rental.dto;

import java.time.Instant;

public record ChatRoomDto(
        Long    otherId,
        String  otherEmail,
        String  lastText,
        Instant lastAt
) {}
