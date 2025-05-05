// dto/InstrumentDto.java
package com.example.rental.dto;

public record InstrumentDto(Long id, Long ownerId,
                            String title, String description,
                            double pricePerDay, String category,String imageUrl  ) {}
