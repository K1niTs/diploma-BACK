// src/main/java/com/example/rental/dto/BookingDto.java
package com.example.rental.dto;

import java.time.LocalDate;

/**
 * Передаём всё, что нужно клиенту:
 *  – instrumentTitle (для списка)
 *  – paymentUrl     (null, пока платёж не создан)
 */
public record BookingDto(
        Long id,
        Long userId,
        Long instrumentId,
        String instrumentTitle,
        LocalDate startDate,
        LocalDate endDate,
        double totalCost,
        String status,
        String paymentUrl       // может быть null
) {}
