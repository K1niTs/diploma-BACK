// Booking.java
package com.example.rental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) private User  user;
    @ManyToOne(optional = false) private Instrument instrument;

    private LocalDate startDate;
    private LocalDate endDate;
    private double   totalCost;
    private String   status;   // NEW, PAID, CANCELLED
    /** URL платёжной формы (заполняется при pay). Может быть null */
    @Column(name = "payment_url")
    private String paymentUrl;                // ← ДОБАВЛЕНО
}
