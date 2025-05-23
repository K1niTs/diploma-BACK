package com.example.rental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name = "reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","instrument_id"}))
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) private User       user;
    @ManyToOne(optional = false) private Instrument instrument;

    @Column(nullable = false) private int     rating;
    @Column(nullable = false) private String  comment;
    @Column(nullable = false) private Instant createdAt = Instant.now();
}
