package com.example.rental.repo;

import com.example.rental.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review,Long> {

    List<Review> findByInstrument_Id(Long instrId);

    boolean existsByUser_IdAndInstrument_Id(Long uid, Long instrId);
}
