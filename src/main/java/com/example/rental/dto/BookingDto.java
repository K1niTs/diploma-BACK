package com.example.rental.dto;

import java.time.LocalDate;


public record BookingDto(
        Long id,
        Long userId,
        Long instrumentId,
        String instrumentTitle,
        LocalDate startDate,
        LocalDate endDate,
        double totalCost,
        String status,
        String paymentUrl
) {}
