package com.example.rental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instruments")
@Getter @Setter @NoArgsConstructor
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User owner;

    private String title;
    private String description;
    private double pricePerDay;
    private String category;

    private String imageUrl;

    @OneToMany(
            mappedBy      = "instrument",
            cascade       = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(
            mappedBy      = "instrument",
            cascade       = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Review> reviews = new ArrayList<>();
}
