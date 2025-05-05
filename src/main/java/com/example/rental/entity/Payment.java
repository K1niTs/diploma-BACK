// Payment.java  — заглушка
package com.example.rental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false) private Booking booking;

    private double amount;
    private String paymentMethod;   // FAKE
    private String paymentStatus;   // SUCCESS
    private Instant createdAt = Instant.now();
}
