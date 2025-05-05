// Instrument.java
package com.example.rental.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instruments")
@Getter @Setter @NoArgsConstructor
public class Instrument {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)       // владелец
    private User owner;

    private String title;
    private String description;
    private double pricePerDay;
    private String category;

    /** URL до картинки, может быть null */
    private String imageUrl;
}