package com.example.rental.repo;

import com.example.rental.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepo
        extends JpaRepository<Instrument, Long>,
        JpaSpecificationExecutor<Instrument> {
}
