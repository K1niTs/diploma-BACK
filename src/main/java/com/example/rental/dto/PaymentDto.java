package com.example.rental.dto;
public record PaymentDto(Long id,Long bookingId,double amount,
                         String paymentMethod,String paymentStatus){}
