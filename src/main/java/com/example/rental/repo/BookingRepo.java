package com.example.rental.repo;

import com.example.rental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    @Query("""
        select count(b) 
        from   Booking b
        where  b.instrument.id = :instId
          and  b.status        <> 'CANCELLED'
          and  b.startDate     <= :to
          and  b.endDate       >= :from
        """)
    long countOverlaps(Long instId,
                       LocalDate from,
                       LocalDate to);
    List<Booking> findByUserId(Long userId);
    boolean existsByUser_IdAndInstrument_IdAndStatusIn(
            Long uid, Long instrId, List<String> statuses);

}
